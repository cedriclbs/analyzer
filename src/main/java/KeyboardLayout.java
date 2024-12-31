package main.java;

import java.util.HashMap;
import java.util.Map;

/**
 * Stocke la disposition : pour chaque caractère, on sait
 * quelle touche (Key) est associée (row, column, main, finger...).
 */
public class KeyboardLayout {

    private final Map<Character, Key> keyByChar;

    public KeyboardLayout() {
        this.keyByChar = new HashMap<>();
    }

    /**
     * Ajout d'une touche dans la disposition (rang, colonne, doigt, main, caractère).
     */
    public void addKey(Key k) {
        keyByChar.put(k.character(), k);
    }

    /**
     * Retrouve la touche (Key) associée à un caractère c.
     * Peut renvoyer null si le caractère n'est pas défini dans la layout.
     */
    public Key findKeyForCharacter(char c) {
        return keyByChar.get(c);
    }

    /**
     * Ajoute toutes les lettres de 'a' à 'z' dans la layout,
     * avec une logique de distribution simplifiée :
     *  - On place les 13 premières lettres (a-m) sur la main LEFT,
     *    les 13 suivantes (n-z) sur la main RIGHT.
     *  - On utilise "col" = 1..26 pour séparer les lettres.
     *  - On assigne le doigt (Finger) en fonction de col % 4 (pinky, ring, middle, index).
     *  - row = 1 pour tout le monde
     */
    public void addAllLetters() {
        int row = 1;
        int col = 1;

        for (char c = 'a'; c <= 'z'; c++) {
            // Séparation main gauche / main droite
            Hand hand = (col <= 13) ? Hand.LEFT : Hand.RIGHT;

            // On définit le doigt en fonction de col modulo 4
            int mod = (col - 1) % 4;
            Finger finger;
            switch (mod) {
                case 0 -> finger = Finger.PINKY;
                case 1 -> finger = Finger.RING;
                case 2 -> finger = Finger.MIDDLE;
                default -> finger = Finger.INDEX;
            }

            // Ajout de la touche
            addKey(new Key(row, col, finger, hand, c));

            col++;
        }
    }

}
