import logiciel1.*;
import logiciel2.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage :  build/libs/les-biens-lothsavan-projet-cpoo5-24-25-all.jar");
            System.out.println("Options :");
            System.out.println("  1 -> Analyseur de texte");
            System.out.println("  2 -> Évaluateur de disposition clavier");
            return;
        }

        String choix = args[0];
        switch (choix) {
            case "1" -> Main1.main(new String[0]);
            case "2" -> Main2.main(new String[0]);
            default -> System.out.println("Option invalide. Utilisez 1 ou 2.");
        }
    }
}
