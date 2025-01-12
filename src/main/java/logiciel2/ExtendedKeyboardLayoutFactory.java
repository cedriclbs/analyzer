package logiciel2;

import java.util.List;

/**
 * La classe ExtendedKeyboardLayoutFactory est une fabrique pour créer des objets ExtendedKeyboardLayout
 * à partir d'un objet KeyboardsJson et d'un nom de disposition (ex. : "FR" ou "EN").
 */
public class ExtendedKeyboardLayoutFactory {

     /**
     * Construit un ExtendedKeyboardLayout à partir des données d'un KeyboardsJson et d'un layoutName.
     *
     * @param keyboardsJson l'objet KeyboardsJson contenant les définitions des dispositions de clavier.
     * @param layoutName le nom de la disposition à utiliser (par exemple, "FR" pour le français ou "EN" pour l'anglais).
     * @return un objet ExtendedKeyboardLayout initialisé avec les touches correspondant à la disposition spécifiée.
     * @throws RuntimeException si le layoutName est inconnu (non défini dans keyboardsJson).
     */
    public static ExtendedKeyboardLayout buildLayout(KeyboardsJson keyboardsJson, String layoutName) {
        ExtendedKeyboardLayout layout = new ExtendedKeyboardLayout();

        // Récupère la liste KeyDTO pour "FR" ou "EN"
        List<KeyboardsJson.KeyDTO> dtos = keyboardsJson.layouts.get(layoutName);
        if (dtos == null) {
            throw new RuntimeException("Layout inconnu: " + layoutName);
        }

        for (KeyboardsJson.KeyDTO dto : dtos) {
            Finger f = Finger.valueOf(dto.finger);
            Hand h   = Hand.valueOf(dto.hand);
            char c = (dto.character.length() == 1)? dto.character.charAt(0): '\0';
            Key k = new Key(dto.row, dto.column, f, h, c);
            layout.addKey(dto.character, k);
        }

        return layout;
    }
}
