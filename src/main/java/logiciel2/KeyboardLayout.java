package logiciel2;

import java.util.HashMap;
import java.util.Map;

/**
 * La classe {@code KeyboardLayout} représente la disposition d'un clavier.
 * Elle associe chaque caractère à une touche physique ({@link Key}), 
 * permettant de retrouver des informations telles que la position (rangée, colonne), 
 * la main, le doigt, et le caractère associé.
 */
public class KeyboardLayout {

    /**
     * Une map associant chaque caractère à une instance de {@link Key}.
     */
    private final Map<Character, Key> keyByChar;

    /**
     * Constructeur par défaut. Initialise une disposition de clavier vide.
     */
    public KeyboardLayout() {
        this.keyByChar = new HashMap<>();
    }

    /**
     * Ajoute une touche à la disposition du clavier.
     *
     * @param k la touche à ajouter, représentée par une instance de {@link Key}
     */
    public void addKey(Key k) {
        keyByChar.put(k.character(), k);
    }

    /**
     * Retrouve la touche associée à un caractère donné.
     *
     * @param c le caractère pour lequel on souhaite retrouver la touche
     * @return l'instance de {@link Key} associée au caractère, ou {@code null}
     * si le caractère n'est pas défini dans la disposition
     */
    public Key findKeyForCharacter(char c) {
        return keyByChar.get(c);
    }

}
