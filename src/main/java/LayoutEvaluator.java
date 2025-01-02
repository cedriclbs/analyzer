package main.java;

import main.java.NGramFrequency;
import java.util.List;

/**
 * Calcule un "score" global d'une disposition de clavier.
 * 
 * 1) on regarde la répartition mainG/mainD sur les 1-grammes
 * 2) on pénalise/récompense certains bigrammes
 * 3) idem pour trigrammes
 * 4) on renvoie le score final
 */
public class LayoutEvaluator {

    // on imagine des poids.
    private final double weightSfb; 
    private final double weightCiseau;
    private final double weightLsb;
    private final double weightRoulement;
    private final double weightAlternance;
    private final double weightRedirection;
    private final double weightMauvaiseRedirection;
    private final double weightSkipgram;

    public LayoutEvaluator(double weightSfb,double weightCiseau,double weightLsb,double weightRoulement,double weightAlternance,double weightRedirection,double weightMauvaiseRedirection,double weightSkipgram) {
        this.weightSfb = weightSfb;
        this.weightCiseau = weightCiseau;
        this.weightLsb = weightLsb;
        this.weightRoulement = weightRoulement;
        this.weightAlternance = weightAlternance;
        this.weightRedirection = weightRedirection;
        this.weightMauvaiseRedirection = weightMauvaiseRedirection;
        this.weightSkipgram = weightSkipgram;
    }

    /**
     * Méthode principale d'évaluation.
     * 
     * @param unigrams  liste de NGramFrequency (1-grammes)
     * @param bigrams   liste de NGramFrequency (2-grammes)
     * @param trigrams  liste de NGramFrequency (3-grammes)
     * @param layout    la disposition de clavier
     * @return          un score global (plus il est élevé, mieux c'est)
     */
    public double evaluateLayout(
            List<NGramFrequency> unigrams,
            List<NGramFrequency> bigrams,
            List<NGramFrequency> trigrams,
            KeyboardLayout layout
    ) {
        long totalOccurrences = 0;
        for (NGramFrequency f : unigrams)  totalOccurrences += f.frequency();
        for (NGramFrequency f : bigrams)   totalOccurrences += f.frequency();
        for (NGramFrequency f : trigrams)  totalOccurrences += f.frequency();

        double totalScore = 0.0;

        // Répartition main gauche / droite sur les unigrams
        totalScore += computeUnigramScore(unigrams, layout);

        // Score sur les bigrammes
        for (NGramFrequency f : bigrams) {
            String bigram = f.nGram();
            if (bigram.length() != 2) continue;

            Key k1 = layout.findKeyForCharacter(bigram.charAt(0));
            Key k2 = layout.findKeyForCharacter(bigram.charAt(1));
            if (k1 == null || k2 == null) continue; // inconnu dans la layout

            MovementType mt = MovementDetector.detectBigramMovement(k1, k2);
            double movementScore = bigramMovementScore(mt);
            totalScore += movementScore * f.frequency();
        }

        // Score sur les trigrammes
        for (NGramFrequency f : trigrams) {
            String trigram = f.nGram();
            if (trigram.length() != 3) continue;

            Key k1 = layout.findKeyForCharacter(trigram.charAt(0));
            Key k2 = layout.findKeyForCharacter(trigram.charAt(1));
            Key k3 = layout.findKeyForCharacter(trigram.charAt(2));
            if (k1 == null || k2 == null || k3 == null) continue;

            MovementType mt = MovementDetector.detectTrigramMovement(k1, k2, k3);
            double movementScore = trigramMovementScore(mt);
            totalScore += movementScore * f.frequency();
        }

        // Normalisation éventuelle (pour comparer plusieurs layouts)
        double normalized = totalScore / (double) totalOccurrences;
        return normalized;
    }

    /**
     * on pénalise le déséquilibre main gauche/droite
     * en calculant un petit malus. 
     */
    private double computeUnigramScore(List<NGramFrequency> unigrams, KeyboardLayout layout) {
        long leftCount = 0;
        long rightCount = 0;
        for (NGramFrequency f : unigrams) {
            if (f.nGram().length() != 1) continue;
            char c = f.nGram().charAt(0);
            Key k = layout.findKeyForCharacter(c);
            if (k != null) {
                if (k.hand() == Hand.LEFT) {
                    leftCount += f.frequency();
                } else {
                    rightCount += f.frequency();
                }
            }
        }
        // On calcule la proportion main gauche
        long total = leftCount + rightCount;
        if (total == 0) return 0.0;
        double leftRatio = (double)leftCount / (double)total;
        // On pénalise l'écart à 50% (c'est arbitraire)
        double imbalance = Math.abs(leftRatio - 0.5);
        // Petit malus => plus l'imbalance est grand, plus on retire de points
        return -(imbalance * 1000.0); 
    }

    /**
     * Calcule un bonus/malus pour un bigram donné son type.
     */
    private double bigramMovementScore(MovementType mt) {
        return switch (mt) {
            case SFB -> -weightSfb;
            case CISEAU -> -weightCiseau;
            case LSB -> -weightLsb;
            case ROULEMENT -> weightRoulement;
            case ALTERNANCE -> weightAlternance;
            default -> 0.0;
        };
    }

    /**
     * Calcule un bonus/malus pour un trigram.
     */
    private double trigramMovementScore(MovementType mt) {
        return switch (mt) {
            case REDIRECTION -> -weightRedirection;
            case MAUVAISE_REDIRECTION -> -weightMauvaiseRedirection;
            case SKIPGRAM -> -weightSkipgram;
            default -> 0.0;
        };
    }
}
