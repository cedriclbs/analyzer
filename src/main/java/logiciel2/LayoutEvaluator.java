package logiciel2;

import logiciel1.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La classe {@code LayoutEvaluator} permet de calculer un score global
 * pour évaluer une disposition de clavier en fonction de plusieurs critères.
 *
 * Les critères évalués incluent :
 * 1. La répartition entre main gauche et main droite pour les unigrams.
 * 2. La répartition par doigts pour les unigrams.
 * 3. Les mouvements impliqués dans les bigrammes (avec bonus/malus).
 * 4. Les mouvements impliqués dans les trigrammes (avec bonus/malus).
 * 5. Un score final normalisé par le nombre total d'occurrences.
 */
public class LayoutEvaluator {

    // Poids pour différents types de mouvements et critères
    private final double weightSfb; // Poids pour les mouvements SFB
    private final double weightCiseau; // Poids pour les mouvements en ciseau
    private final double weightLsb; // Poids pour les mouvements LSB
    private final double weightRoulement; // Bonus pour le roulement
    private final double weightAlternance; // Bonus pour l'alternance
    private final double weightRedirection; // Malus pour la redirection
    private final double weightMauvaiseRedirection; // Malus pour mauvaise redirection
    private final double weightSkipgram; // Malus pour les mouvements skipgram
    private final double weightFingerDistribution; // Poids pour l'écart de répartition par doigt

    // Répartition idéale par doigt (exprimée en pourcentage des frappes totales)
    private final Map<Finger, Double> idealFingerDistribution = Map.of(
        Finger.PINKY,  0.10, // 10 %
        Finger.RING,   0.15, // 15 %
        Finger.MIDDLE, 0.20, // 20 %
        Finger.INDEX,  0.45, // 45 %
        Finger.THUMB,  0.10  // 10 %
    );

    /**
     * Constructeur pour initialiser les poids de chaque critère.
     *
     * @param weightSfb poids pour les mouvements SFB
     * @param weightCiseau poids pour les mouvements en ciseau
     * @param weightLsb poids pour les mouvements LSB
     * @param weightRoulement bonus pour le roulement
     * @param weightAlternance bonus pour l'alternance
     * @param weightRedirection malus pour les redirections
     * @param weightMauvaiseRedirection malus pour les mauvaises redirections
     * @param weightSkipgram malus pour les skipgrams
     * @param weightFingerDistribution poids pour l'écart par doigt
     */
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
     * Évalue une disposition de clavier en calculant un score global.
     *
     * @param unigrams  la liste des unigrams avec leur fréquence
     * @param bigrams   la liste des bigrams avec leur fréquence
     * @param trigrams  la liste des trigrammes avec leur fréquence
     * @param layout    la disposition de clavier à évaluer
     * @return un score global, normalisé par le nombre total d'occurrences
     */
    public double evaluateLayout(
            List<NGramFrequency> unigrams,
            List<NGramFrequency> bigrams,
            List<NGramFrequency> trigrams,
            KeyboardLayout layout
    ) {
        long totalOccurrences = 0;
        for (NGramFrequency f : unigrams) totalOccurrences += f.frequency();
        for (NGramFrequency f : bigrams) totalOccurrences += f.frequency();
        for (NGramFrequency f : trigrams) totalOccurrences += f.frequency();

        double totalScore = 0.0;

        // 1) Répartition main gauche / droite
        totalScore += computeHandImbalanceScore(unigrams, layout);

        // 2) Répartition par doigts
        totalScore += computeFingerImbalanceScore(unigrams, layout);

        // 3) Bigrammes
        for (NGramFrequency f : bigrams) {
            String bigram = f.nGram();
            if (bigram.length() != 2) continue;

            Key k1 = layout.findKeyForCharacter(bigram.charAt(0));
            Key k2 = layout.findKeyForCharacter(bigram.charAt(1));
            if (k1 == null || k2 == null) continue;

            MovementType mt = MovementDetector.detectBigramMovement(k1, k2);
            double movementScore = bigramMovementScore(mt);
            totalScore += movementScore * f.frequency();
        }

        // 4) Trigrammes
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

        // Normalisation
        return totalOccurrences == 0 ? totalScore : totalScore / (double) totalOccurrences;
    }

    /**
     * Calcule un malus en cas de déséquilibre main gauche / main droite.
     *
     * @param unigrams liste des unigrams avec leurs fréquences
     * @param layout   la disposition de clavier
     * @return un malus proportionnel au déséquilibre
     */
    private double computeHandImbalanceScore(List<NGramFrequency> unigrams, KeyboardLayout layout) {
        long leftCount = 0;
        long rightCount = 0;
        for (NGramFrequency f : unigrams) {
            if (f.nGram().length() != 1) continue;
            char c = f.nGram().charAt(0);
            Key k = layout.findKeyForCharacter(c);
            if (k != null) {
                if (k.hand() == Hand.LEFT) leftCount += f.frequency();
                else rightCount += f.frequency();
            }
        }
        long total = leftCount + rightCount;
        if (total == 0) return 0.0;

        double imbalance = Math.abs((double) leftCount / total - 0.5);
        return -(imbalance * 1000.0);
    }

    /**
     * Calcule un malus basé sur la répartition réelle par doigt par rapport à une répartition idéale.
     *
     * @param unigrams liste des unigrams avec leurs fréquences
     * @param layout   la disposition de clavier
     * @return un malus basé sur la différence entre la répartition réelle et idéale
     */
    private double computeFingerImbalanceScore(List<NGramFrequency> unigrams, KeyboardLayout layout) {
        Map<Finger, Long> countByFinger = new HashMap<>();
        long total = 0;

        for (NGramFrequency f : unigrams) {
            if (f.nGram().length() != 1) continue;
            char c = f.nGram().charAt(0);
            Key k = layout.findKeyForCharacter(c);
            if (k != null) {
                countByFinger.put(k.finger(), countByFinger.getOrDefault(k.finger(), 0L) + f.frequency());
                total += f.frequency();
            }
        }

        if (total == 0) return 0.0;

        double sumMalus = 0.0;
        for (Finger finger : idealFingerDistribution.keySet()) {
            double idealRatio = idealFingerDistribution.get(finger);
            long realCount = countByFinger.getOrDefault(finger, 0L);
            double realRatio = (double) realCount / total;

            sumMalus += -500.0 * Math.abs(realRatio - idealRatio) * weightFingerDistribution;
        }

        return sumMalus;
    }

    /**
     * Calcule un bonus ou un malus pour un bigram en fonction de son type de mouvement.
     *
     * @param mt le type de mouvement
     * @return un score ajusté pour ce type de mouvement
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
     * Calcule un bonus ou un malus pour un trigram en fonction de son type de mouvement.
     *
     * @param mt le type de mouvement
     * @return un score ajusté pour ce type de mouvement
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
