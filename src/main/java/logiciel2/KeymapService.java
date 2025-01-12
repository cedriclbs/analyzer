package logiciel2;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code KeymapService} fournit des services utilitaires
 * pour transformer un caractère en une séquence de touches physiques
 * en utilisant une combinaison d'un keymap et d'une disposition étendue de clavier.
 */
public class KeymapService {

    /**
     * Transforme un caractère donné en une liste de touches physiques ({@code Key}),
     * en utilisant les correspondances définies dans un keymap JSON (char -> labels)
     * et une disposition de clavier étendue (label -> Key).
     *
     * @param c le caractère à convertir en séquence de touches (par exemple, 'A').
     * @param layout l'objet {@code ExtendedKeyboardLayout} qui contient la correspondance label -> Key.
     * @param keymap l'objet {@code KeymapJson} contenant la correspondance char -> séquence de labels.
     * @return une liste de touches physiques correspondant au caractère. Si aucune correspondance n'est trouvée,
     *         une liste vide est retournée.
     */
    public static List<Key> getPhysicalKeysForChar(
            char c,
            ExtendedKeyboardLayout layout,
            KeymapJson keymap
    ) {
        String s = String.valueOf(c);
        List<String> labels = keymap.charToKeySequence.get(s);
        if (labels == null) {
            Key single = layout.findKey(s);
            if (single != null) {
                return List.of(single);
            }
            return List.of();
        }
        List<Key> result = new ArrayList<>();
        for (String lbl : labels) {
            Key found = layout.findKey(lbl);
            if (found != null) {
                result.add(found);
            }
        }
        return result;
    }
}
