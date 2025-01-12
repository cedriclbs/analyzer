package logiciel2;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

/**
 * Classe utilitaire pour charger un fichier JSON contenant un keymap
 * (généralement nommé "keymap.json") et le désérialiser en un objet {@code KeymapJson}.
 */
public class KeymapJsonLoader {

    /**
     * Charge un fichier JSON représentant un keymap depuis le classpath
     * et le convertit en un objet de type {@code KeymapJson}.
     *
     * @param resourcePath le chemin relatif du fichier JSON dans le classpath.
     *                     Par exemple, "resources/keymap.json".
     * @return un objet {@code KeymapJson} représentant le contenu du fichier JSON.
     * @throws RuntimeException si le fichier JSON est introuvable ou si une erreur survient
     *                          lors de sa lecture ou de sa désérialisation.
     */
    public static KeymapJson loadKeymap(String resourcePath) {
        try {
            InputStream input = KeymapJsonLoader.class
                .getClassLoader()
                .getResourceAsStream(resourcePath);

            if (input == null) {
                throw new RuntimeException("Fichier keymap.json introuvable: " + resourcePath);
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(input, KeymapJson.class);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement keymap.json: " + resourcePath, e);
        }
    }
}
