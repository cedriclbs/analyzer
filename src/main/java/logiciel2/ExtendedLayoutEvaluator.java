package logiciel2;

import logiciel1.NGramFrequency;
import java.util.List;

/**
 * La classe ExtendedLayoutEvaluator est un évaluateur avancé qui prend en compte un keymap
 * pour évaluer une disposition de clavier étendue (ExtendedKeyboardLayout).
 * Elle utilise des fréquences de n-grammes (unigrams, bigrams, trigrams) pour calculer un score global.
 */
public class ExtendedLayoutEvaluator {

    private final double weightSfb;
    private final double weightCiseau;
    private final double weightLsb;
    private final double weightRoulement;
    private final double weightAlternance;
    private final double weightRedirection;
    private final double weightMauvaiseRedirection;
    private final double weightSkipgram;


    /**
     * Constructeur pour initialiser les poids des différents types de mouvements.
     *
     * @param weightSfb poids associé au Same-Finger Bigram.
     * @param weightCiseau poids associé au mouvement en "ciseau".
     * @param weightLsb poids associé au Lateral Stretch Bigram.
     * @param weightRoulement poids associé au roulement.
     * @param weightAlternance poids associé à l'alternance entre mains.
     * @param weightRedirection poids associé à la redirection.
     * @param weightMauvaiseRedirection poids associé à la mauvaise redirection.
     * @param weightSkipgram poids associé au skipgram (décalage de touches).
     */
    public ExtendedLayoutEvaluator(
            double weightSfb,
            double weightCiseau,
            double weightLsb,
            double weightRoulement,
            double weightAlternance,
            double weightRedirection,
            double weightMauvaiseRedirection,
            double weightSkipgram
    ) {
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
     * Évalue une disposition de clavier en calculant un score global basé sur 
     * les fréquences des bigrams et trigrams dans un corpus donné.
     *
     * @param unigrams liste des unigrams avec leurs fréquences.
     * @param bigrams liste des bigrams avec leurs fréquences.
     * @param trigrams liste des trigrams avec leurs fréquences.
     * @param extLayout un ExtendedKeyboardLayout contenant la correspondance label -> Key.
     * @param keymap un KeymapJson contenant la correspondance char -> liste de labels.
     * @return le score global calculé pour le layout.
     */
    public double evaluate(
            List<NGramFrequency> unigrams,
            List<NGramFrequency> bigrams,
            List<NGramFrequency> trigrams,
            ExtendedKeyboardLayout extLayout,
            KeymapJson keymap
    ) {
        long totalOccurrences = 0;
        for (NGramFrequency f : unigrams) totalOccurrences += f.frequency();
        for (NGramFrequency f : bigrams)  totalOccurrences += f.frequency();
        for (NGramFrequency f : trigrams) totalOccurrences += f.frequency();

        double totalScore = 0.0;

        for (NGramFrequency freq : bigrams) {
            String bigram = freq.nGram();
            if (bigram.length() != 2) continue; // par sécurité

            // On convertit ce bigram en touches
            // ex: "î" => ["^","i"]  +  "l" => ["l"] => 3 touches
            List<Key> seq1 = KeymapService.getPhysicalKeysForChar(bigram.charAt(0), extLayout, keymap);
            List<Key> seq2 = KeymapService.getPhysicalKeysForChar(bigram.charAt(1), extLayout, keymap);
            List<Key> combined = concat(seq1, seq2);

            // Si on a EXACTEMENT 2 touches => bigram
            if (combined.size() == 2) {
                MovementType mt = MovementDetector.detectBigramMovement(combined.get(0), combined.get(1));
                double movementScore = bigramMovementScore(mt);
                totalScore += movementScore * freq.frequency();
            }
            // Si on a EXACTEMENT 3 touches => trigram
            else if (combined.size() == 3) {
                MovementType mt = MovementDetector.detectTrigramMovement(combined.get(0), combined.get(1), combined.get(2));
                double movementScore = trigramMovementScore(mt);
                totalScore += movementScore * freq.frequency();
            }
        }

        for (NGramFrequency freq : trigrams) {
            String trigram = freq.nGram();
            if (trigram.length() != 3) continue;

            List<Key> s1 = KeymapService.getPhysicalKeysForChar(trigram.charAt(0), extLayout, keymap);
            List<Key> s2 = KeymapService.getPhysicalKeysForChar(trigram.charAt(1), extLayout, keymap);
            List<Key> s3 = KeymapService.getPhysicalKeysForChar(trigram.charAt(2), extLayout, keymap);
            List<Key> combined = concat(s1, s2, s3);

            if (combined.size() == 3) {
                MovementType mt = MovementDetector.detectTrigramMovement(
                        combined.get(0),
                        combined.get(1),
                        combined.get(2)
                );
                double movementScore = trigramMovementScore(mt);
                totalScore += movementScore * freq.frequency();
            }
        }
        return (totalOccurrences == 0) ? totalScore : (totalScore / totalOccurrences);
    }

    /**
     * Calcule le score pour un bigram en fonction du type de mouvement.
     *
     * @param mt le type de mouvement détecté.
     * @return le score associé au mouvement.
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
     * Calcule le score pour un trigram en fonction du type de mouvement.
     *
     * @param mt le type de mouvement détecté.
     * @return le score associé au mouvement.
     */
    private double trigramMovementScore(MovementType mt) {
        return switch (mt) {
            case REDIRECTION -> -weightRedirection;
            case MAUVAISE_REDIRECTION -> -weightMauvaiseRedirection;
            case SKIPGRAM -> -weightSkipgram;
            default -> 0.0;
        };
    }

     /**
     * Concatène deux listes de Key.
     *
     * @param a première liste.
     * @param b deuxième liste.
     * @return une nouvelle liste contenant les éléments de a suivis de ceux de b.
     */
    private List<Key> concat(List<Key> a, List<Key> b) {
        java.util.ArrayList<Key> res = new java.util.ArrayList<>(a);
        res.addAll(b);
        return res;
    }

    /**
     * Concatène trois listes de Key.
     *
     * @param a première liste.
     * @param b deuxième liste.
     * @param c troisième liste.
     * @return une nouvelle liste contenant les éléments de a suivis de ceux de b et de c.
     */
    private List<Key> concat(List<Key> a, List<Key> b, List<Key> c) {
        java.util.ArrayList<Key> res = new java.util.ArrayList<>(a);
        res.addAll(b);
        res.addAll(c);
        return res;
    }
}
