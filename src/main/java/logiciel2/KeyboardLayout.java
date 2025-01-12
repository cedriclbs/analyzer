package logiciel2;

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

}
