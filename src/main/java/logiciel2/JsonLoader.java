package logiciel2;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

/**
 * La classe {@code JsonLoader} fournit une méthode utilitaire pour charger
 * et parser des fichiers JSON en objets Java en utilisant la bibliothèque Jackson.
 */
public class JsonLoader {

   /**
     * Charge un fichier JSON depuis le classpath et le convertit en un objet de type {@code KeyboardsJson}.
     * @param resourcePath le chemin relatif du fichier JSON dans le classpath.
     * Par exemple, "resources/keyboards.json".
     * @return un objet {@code KeyboardsJson} représentant le contenu du fichier JSON.
     * @throws RuntimeException si le fichier JSON est introuvable ou si une erreur se produit
     * lors de la lecture ou du parsing.
     */
    public static KeyboardsJson loadKeyboards(String resourcePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            
            InputStream input = JsonLoader.class.getClassLoader().getResourceAsStream(resourcePath);
            if (input == null) {
                throw new RuntimeException("Fichier JSON non trouvé : " + resourcePath);
            }
    
            return mapper.readValue(input, KeyboardsJson.class);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture du JSON : " + resourcePath, e);
        }
    }
    
}
