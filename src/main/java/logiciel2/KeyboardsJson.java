package logiciel2;

import java.util.List;
import java.util.Map;

/**
 * La classe {@code KeyboardsJson} représente une structure de données pour charger des
 * dispositions de claviers à partir d'un fichier JSON.
 * Chaque disposition est identifiée par un nom (par exemple, "AZERTY") et associe
 * ce nom à une liste d'objets {@link KeyDTO} décrivant les touches du clavier.
 */
public class KeyboardsJson {

    /**
     * Une map représentant les dispositions de claviers.
     * La clé est le nom de la disposition (par exemple, "AZERTY"), et la valeur est
     * une liste de {@link KeyDTO} décrivant les touches.
     */
    public Map<String, List<KeyDTO>> layouts;

    /**
     * La classe interne {@code KeyDTO} représente une touche au sein d'une disposition de clavier.
     * Elle est utilisée pour charger et sérialiser les données JSON décrivant les touches.
     */
    public static class KeyDTO {
        public String character;

        /**
         * L'indice de la rangée où se trouve la touche sur le clavier.
         */
        public int row;

        /**
         * L'indice de la colonne où se trouve la touche sur le clavier.
         */
        public int column;

        /**
         * Le doigt utilisé pour appuyer sur cette touche, sous forme de chaîne.
         * Exemple : "INDEX", "THUMB".
         */
        public String finger;

        /**
         * La main utilisée pour cette touche, sous forme de chaîne.
         * Exemple : "LEFT", "RIGHT".
         */
        public String hand;

        /**
         * Constructeur vide requis pour la désérialisation avec Jackson.
         */
        public KeyDTO() {
            // Jackson a besoin d'un constructeur vide
        }
    }
}
