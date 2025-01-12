import logiciel1.*;
import logiciel2.*;

/**
 * La classe {@code Main} est le point d'entrée principal du programme.
 * Elle offre deux fonctionnalités principales selon l'option choisie :
 * 1. Analyseur de texte.
 * 2. Évaluateur de disposition clavier.
 */
public class Main {

    /**
     * Méthode principale du programme.
     * En fonction de l'option passée en argument, elle redirige vers les fonctionnalités :
     * 1 -> Lancement de l'analyseur de texte (via {@link Main1}).
     * 2 -> Lancement de l'évaluateur de disposition clavier (via {@link Main2}).
     *
     * @param args les arguments de la ligne de commande :
     *             <ul>
     *               <li>1 : Lancer l'analyseur de texte.</li>
     *               <li>2 : Lancer l'évaluateur de disposition clavier.</li>
     *             </ul>
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage : build/libs/les-biens-lothsavan-projet-cpoo5-24-25-all.jar");
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
