package logiciel2;

import java.util.HashMap;
import java.util.Map;

/**
 * La classe ExtendedKeyboardLayout permet de stocker la correspondance entre un label (ex. : "a", "Shift") 
 * et une touche physique de clavier représentée par un objet Key.
 *
 * Exemple :
 * <pre>
 * ExtendedKeyboardLayout layout = new ExtendedKeyboardLayout();
 * layout.addKey("a", new Key(1, 1, Finger.PINKY, Hand.LEFT, 'a'));
 * layout.addKey("Shift", new Key(4, 1, Finger.PINKY, Hand.LEFT, '\0'));
 *
 * Key key = layout.findKey("a");
 * </pre>
 */
public class ExtendedKeyboardLayout {

    private final Map<String, Key> keysByLabel;

    /**
     * Construit un ExtendedKeyboardLayout vide.
     * Initialise la structure interne pour stocker les correspondances label-touche.
     */
    public ExtendedKeyboardLayout() {
        this.keysByLabel = new HashMap<>();
    }

    /**
     * Ajoute une touche (Key) à la disposition du clavier sous un label donné.
     *
     * @param label le label associé à la touche (ex. : "a", "Shift").
     * @param key l'objet Key représentant la touche physique à associer au label.
     * @throws NullPointerException si le label ou la touche est null.
     */
    public void addKey(String label, Key key) {
        keysByLabel.put(label, key);
    }

     /**
     * Récupère la touche physique associée au label donné.
     *
     * @param label le label associé à la touche recherchée (ex. : "a", "Shift").
     * @return l'objet Key correspondant au label, ou {@code null} si aucune correspondance n'existe.
     */
    public Key findKey(String label) {
        return keysByLabel.get(label);
    }
}
