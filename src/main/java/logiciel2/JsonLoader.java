package logiciel2;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class JsonLoader {

    public static KeyboardsJson loadKeyboards(String resourcePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Charger le fichier JSON depuis le classpath
            InputStream input = JsonLoader.class.getClassLoader().getResourceAsStream(resourcePath);
            if (input == null) {
                throw new RuntimeException("Fichier JSON non trouvé : " + resourcePath);
            }

            // Lire le JSON et le convertir en objet KeyboardsJson
            return mapper.readValue(input, KeyboardsJson.class);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture du JSON : " + resourcePath, e);
        }
    }
}
