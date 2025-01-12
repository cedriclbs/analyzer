package main.java;

import java.util.List;
import java.util.Map;


public class KeyboardsJson {
    public Map<String, List<KeyDTO>> layouts;  // "AZERTY" -> liste de KeyDTO

    // Classe interne pour décrire un "objet touche" (character, row, col, finger, hand)
    public static class KeyDTO {
        public String character;
        public int row;
        public int column;
        public String finger;
        public String hand;

        public KeyDTO() {
            // Jackson a besoin d'un constructeur vide
        }
    }
}
