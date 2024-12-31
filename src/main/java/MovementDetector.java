package main.java;

import static java.lang.Math.abs;

/**
 * Classe utilitaire : on lui donne 2 ou 3 touches,
 * elle nous dit si c'est un SFB, un ciseau, un skipgram, etc.
 */
public class MovementDetector {

    /**
     * Détecte le type d'un bigram (2 touches).
     */
    public static MovementType detectBigramMovement(Key k1, Key k2) {
        // Même main, même doigt => SFB (Single-Finger Bigram)
        if (k1.hand() == k2.hand() && k1.finger() == k2.finger()) {
            return MovementType.SFB;
        }

        // Même main ? => ciseau, roulement, extension latérale (LSB), ...
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
     * Détecte le type d'un trigram (3 touches).
     */
    public static MovementType detectTrigramMovement(Key k1, Key k2, Key k3) {
        // Vérifier si les 3 touches sont sur la même main
        boolean sameHand = (k1.hand() == k2.hand()) && (k2.hand() == k3.hand());
        if (!sameHand) {
            // si ce n'est pas la même main, on n'a pas défini de mouvement "spécial"
            return MovementType.UNKNOWN;
        }
        // => c'est la même main, on vérifie redirection, skipgram...
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


    // Méthodes internes pour la détection des bigrammes
    private static boolean isCiseau(Key k1, Key k2) {
        // "Ciseau" : par exemple si la différence de row est >= 2
        return abs(k1.row() - k2.row()) >= 2;
    }

    private static boolean isRoulement(Key k1, Key k2) {
        // "Roulement" : heuristique simplifiée.
        // Par ex, si diff de colonne = 1 sur la même main et le doigt est différent, on appelle ça un roulement.
        if (k1.hand() == k2.hand()) {
            return (abs(k1.column() - k2.column()) == 1)
                    && (k1.finger() != k2.finger());
        }
        return false;
    }

    private static boolean isLateralStretch(Key k1, Key k2) {
        // "LSB" : un grand écart latéral sur la même main => |column1 - column2| >= 2
        return (k1.hand() == k2.hand()) 
                && abs(k1.column() - k2.column()) >= 2;
    }

    // Méthodes internes pour la détection des trigrammes
    private static boolean isRedirection(Key k1, Key k2, Key k3) {
        // Redirection : inversion de direction entre (k1->k2) et (k2->k3)
        int d12 = k2.column() - k1.column();
        int d23 = k3.column() - k2.column();
        // Signe différent => produit < 0
        return (d12 * d23 < 0);
    }

    private static boolean isMauvaiseRedirection(Key k1, Key k2, Key k3) {
        // "Mauvaise" redirection : par exemple, aucune des trois touches n'est sur l'index
        return (k1.finger() != Finger.INDEX
                && k2.finger() != Finger.INDEX
                && k3.finger() != Finger.INDEX);
    }

    private static boolean isSkipgram(Key k1, Key k2, Key k3) {
        // Skipgram (SKS) : k1 et k3 = même doigt/main, k2 = autre main
        // => sauf qu'on a vérifié sameHand avant, donc ici on adaptera la condition :
        // On le déclare skipgram si k1 et k3 sont le même doigt, 
        // et k2 est... sur l'autre main ? (Mais on a "sameHand = true" plus haut.)
        // Ex simplifié : si (k1 == k3) en doigt, c'est un skipgram.
        return (k1.finger() == k3.finger());
    }

}
