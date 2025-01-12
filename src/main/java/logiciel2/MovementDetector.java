package logiciel2;

import static java.lang.Math.abs;

/**
 * La classe utilitaire {@code MovementDetector} permet d'analyser
 * les mouvements entre deux ou trois touches sur un clavier et de
 * les classifier en différents types (exemple : SFB, ciseau, skipgram, etc.).
 */
public class MovementDetector {

    /**
     * Détecte le type de mouvement pour un bigram (deux touches).
     *
     * @param k1 la première touche
     * @param k2 la deuxième touche
     * @return le type de mouvement détecté (exemple : SFB, ciseau, alternance, etc.)
     */
    public static MovementType detectBigramMovement(Key k1, Key k2) {
        // Même main, même doigt => SFB (Single-Finger Bigram)
        if (k1.hand() == k2.hand() && k1.finger() == k2.finger()) {
            return MovementType.SFB;
        }

        // Même main ? => ciseau, roulement, extension latérale (LSB)
        if (k1.hand() == k2.hand()) {
            if (isCiseau(k1, k2)) {
                return MovementType.CISEAU;
            }
            if (isRoulement(k1, k2)) {
                return MovementType.ROULEMENT;
            }
            if (isLateralStretch(k1, k2)) {
                return MovementType.LSB;
            }
        } else {
            // Mains différentes => alternance
            return MovementType.ALTERNANCE;
        }

        // Sinon inconnu
        return MovementType.UNKNOWN;
    }

    /**
     * Détecte le type de mouvement pour un trigram (trois touches).
     *
     * @param k1 la première touche
     * @param k2 la deuxième touche
     * @param k3 la troisième touche
     * @return le type de mouvement détecté (exemple : redirection, skipgram, etc.)
     */
    public static MovementType detectTrigramMovement(Key k1, Key k2, Key k3) {
        // Vérifie si les trois touches sont sur la même main
        boolean sameHand = (k1.hand() == k2.hand()) && (k2.hand() == k3.hand());
        if (!sameHand) {
            return MovementType.UNKNOWN;
        }

        // Analyse des mouvements sur la même main
        if (isRedirection(k1, k2, k3)) {
            if (isMauvaiseRedirection(k1, k2, k3)) {
                return MovementType.MAUVAISE_REDIRECTION;
            }
            return MovementType.REDIRECTION;
        }
        if (isSkipgram(k1, k2, k3)) {
            return MovementType.SKIPGRAM;
        }

        return MovementType.UNKNOWN;
    }

    /**
     * Vérifie si deux touches forment un mouvement en "ciseau".
     *
     * @param k1 la première touche
     * @param k2 la deuxième touche
     * @return {@code true} si le mouvement est un ciseau, {@code false} sinon
     */
    private static boolean isCiseau(Key k1, Key k2) {
        return abs(k1.row() - k2.row()) >= 2;
    }

    /**
     * Vérifie si deux touches forment un "roulement".
     *
     * @param k1 la première touche
     * @param k2 la deuxième touche
     * @return {@code true} si le mouvement est un roulement, {@code false} sinon
     */
    private static boolean isRoulement(Key k1, Key k2) {
        if (k1.hand() == k2.hand()) {
            return (abs(k1.column() - k2.column()) == 1) && (k1.finger() != k2.finger());
        }
        return false;
    }

    /**
     * Vérifie si deux touches forment une extension latérale.
     *
     * @param k1 la première touche
     * @param k2 la deuxième touche
     * @return {@code true} si le mouvement est une extension latérale, {@code false} sinon
     */
    private static boolean isLateralStretch(Key k1, Key k2) {
        return (k1.hand() == k2.hand()) && abs(k1.column() - k2.column()) >= 2;
    }

    /**
     * Vérifie si trois touches forment une redirection.
     *
     * @param k1 la première touche
     * @param k2 la deuxième touche
     * @param k3 la troisième touche
     * @return {@code true} si le mouvement est une redirection, {@code false} sinon
     */
    private static boolean isRedirection(Key k1, Key k2, Key k3) {
        int d12 = k2.column() - k1.column();
        int d23 = k3.column() - k2.column();
        return (d12 * d23 < 0);
    }

    /**
     * Vérifie si trois touches forment une mauvaise redirection.
     *
     * @param k1 la première touche
     * @param k2 la deuxième touche
     * @param k3 la troisième touche
     * @return {@code true} si le mouvement est une mauvaise redirection, {@code false} sinon
     */
    private static boolean isMauvaiseRedirection(Key k1, Key k2, Key k3) {
        return (k1.finger() != Finger.INDEX && k2.finger() != Finger.INDEX && k3.finger() != Finger.INDEX);
    }

    /**
     * Vérifie si trois touches forment un skipgram.
     *
     * @param k1 la première touche
     * @param k2 la deuxième touche
     * @param k3 la troisième touche
     * @return {@code true} si le mouvement est un skipgram, {@code false} sinon
     */
    private static boolean isSkipgram(Key k1, Key k2, Key k3) {
        return (k1.finger() == k3.finger());
    }
}
