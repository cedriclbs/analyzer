package main.java;

import main.java.*;  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calcule un "score" global d'une disposition de clavier.
 * 
 * 1) on regarde la répartition mainG/mainD sur les 1-grammes (déjà existant)
 * 2) on regarde la répartition par doigts (NOUVEAU)
 * 3) on pénalise/récompense certains bigrammes
 * 4) on pénalise/récompense certains trigrammes
 * 5) on renvoie le score final
 */
public class LayoutEvaluator {

    // on imagine des poids (vous pouvez les ajuster à votre guise) 
    private final double weightSfb; 
    private final double weightCiseau;
    private final double weightLsb;
    private final double weightRoulement;
    private final double weightAlternance;
    private final double weightRedirection;
    private final double weightMauvaiseRedirection;
    private final double weightSkipgram;

    // NOUVEAU : on ajoute un poids pour la répartition par doigts
    private final double weightFingerDistribution;

    // Définir une répartition « idéale » par doigt.
    // Ici un exemple arbitraire, à vous d'ajuster.
    // On indique un pourcentage sur l'ensemble des frappes.
    private final Map<Finger, Double> idealFingerDistribution = Map.of(
        Finger.PINKY,  0.10, // 10%
        Finger.RING,   0.15, // 15%
        Finger.MIDDLE, 0.20, // 20%
        Finger.INDEX,  0.45, // 45%
        Finger.THUMB,  0.10  // 10% (si vous gérez l'espace, etc.)
    );

    public LayoutEvaluator(
            double weightSfb,
            double weightCiseau,
            double weightLsb,
            double weightRoulement,
            double weightAlternance,
            double weightRedirection,
            double weightMauvaiseRedirection,
            double weightSkipgram,
            double weightFingerDistribution
    ) {
        this.weightSfb = weightSfb;
        this.weightCiseau = weightCiseau;
        this.weightLsb = weightLsb;
        this.weightRoulement = weightRoulement;
        this.weightAlternance = weightAlternance;
        this.weightRedirection = weightRedirection;
        this.weightMauvaiseRedirection = weightMauvaiseRedirection;
        this.weightSkipgram = weightSkipgram;
        this.weightFingerDistribution = weightFingerDistribution;
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

        // 1) Répartition main gauche / droite sur les unigrams (malus)
        totalScore += computeHandImbalanceScore(unigrams, layout);

        // 2) Répartition par doigts (NOUVEAU malus)
        totalScore += computeFingerImbalanceScore(unigrams, layout);

        // 3) Score sur les bigrammes
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

        // 4) Score sur les trigrammes
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

        // Normalisation par le nombre total d'occurrences
        if (totalOccurrences == 0) {
            return totalScore; 
        } else {
            return totalScore / (double) totalOccurrences;
        }
    }

    /**
     * On pénalise le déséquilibre main gauche/droite
     * en calculant un petit malus (code initial).
     */
    private double computeHandImbalanceScore(List<NGramFrequency> unigrams, KeyboardLayout layout) {
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
        long total = leftCount + rightCount;
        if (total == 0) return 0.0;

        double leftRatio = (double) leftCount / total;
        double imbalance = Math.abs(leftRatio - 0.5);

        // Petit malus => plus l'imbalance est grand, plus on retire de points
        // Ici, -1000.0 * imbalance par défaut (libre à vous de changer)
        return -(imbalance * 1000.0);
    }

    /**
     * NOUVEAU :
     * On calcule la répartition réelle par doigt, 
     * on compare à une répartition idéale (idealFingerDistribution).
     * On applique un malus si on est éloigné de l'idéal.
     */
    private double computeFingerImbalanceScore(List<NGramFrequency> unigrams, KeyboardLayout layout) {
        // 1) Compter le nombre de frappes par doigt
        Map<Finger, Long> countByFinger = new HashMap<>();
        long total = 0;

        for (NGramFrequency f : unigrams) {
            if (f.nGram().length() != 1) continue;
            char c = f.nGram().charAt(0);
            Key k = layout.findKeyForCharacter(c);
            if (k != null) {
                countByFinger.put(k.finger(), 
                    countByFinger.getOrDefault(k.finger(), 0L) + f.frequency()
                );
                total += f.frequency();
            }
        }

        if (total == 0) return 0.0;

        // 2) Calculer la répartition réelle et comparer à l'idéal
        double sumMalus = 0.0;

        for (Finger finger : idealFingerDistribution.keySet()) {
            double idealRatio = idealFingerDistribution.get(finger);
            long realCount = countByFinger.getOrDefault(finger, 0L);
            double realRatio = (double) realCount / total;

            // Mesurer l'écart absolu
            double diff = Math.abs(realRatio - idealRatio);

            // On peut imaginer un malus proportionnel à diff
            // par ex. -500.0 * diff * weightFingerDistribution
            sumMalus += -500.0 * diff * weightFingerDistribution;
        }

        return sumMalus;
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
