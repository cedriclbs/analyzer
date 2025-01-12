package logiciel1;

import java.util.List;
import java.util.Scanner;

/**
 * La classe {@code Main1} est le point d'entrée principal du programme.
 * Elle permet à l'utilisateur de choisir un texte, d'analyser son contenu
 * en n-grammes (unigrams, bigrams, trigrams) et d'exporter les résultats dans un fichier CSV.
 */
public class Main1 {

    /**
     * Point d'entrée principal du programme.
     * Ce programme exécute les étapes suivantes :
     * 1. Demande à l'utilisateur de choisir un fichier texte à analyser parmi plusieurs options.
     * 2. Lit le contenu du fichier choisi.
     * 3. Analyse le texte pour générer les unigrams, bigrams et trigrams.
     * 4. Exporte les résultats dans un fichier CSV.
     *
     * @param args les arguments passés en ligne de commande (non utilisés ici)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1) Demande à l'utilisateur de choisir un texte
        String textChoice;
        String inputFilePath = null;
        do {
            System.out.println("\nChoisissez un texte à analyser :\n");
            System.out.println("1) Poème Français1");
            System.out.println("2) Poème Français2");
            System.out.println("3) Poème Anglais1");
            System.out.println("4) Poème Anglais2");
            System.out.print("\nVotre choix de 1 à 4 : ");
            textChoice = sc.nextLine().trim();

            switch (textChoice) {
                case "1" -> inputFilePath = "src/main/resources/input/sample-corpus1.txt";
                case "2" -> inputFilePath = "src/main/resources/input/sample-corpus2.txt";
                case "3" -> inputFilePath = "src/main/resources/input/sample-corpus3.txt";
                case "4" -> inputFilePath = "src/main/resources/input/sample-corpus4.txt";
                default -> {
                    System.out.println("\nChoix invalide ! Veuillez entrer un nombre entre 1 et 4.\n");
                    inputFilePath = null; 
                }
            }
        } while (inputFilePath == null);

        // 2) Lire le fichier choisi
        FileReader fileReader = new FileReader();
        String corpusContent = fileReader.readFile(inputFilePath);

        // 3) Analyse : on crée les unigrams, bigrams, trigrammes
        CorpusAnalyzer analyzer = new CorpusAnalyzer();
        List<NGramFrequency> unigrams = analyzer.nGramList(corpusContent, 1);
        List<NGramFrequency> bigrams  = analyzer.nGramList(corpusContent, 2);
        List<NGramFrequency> trigrams = analyzer.nGramList(corpusContent, 3);

        // 4) Exporte en CSV
        FileExport exporter = new FileExport();
        String outputCsvPath = "output-ngram1.csv";
        exporter.exportToCsv(outputCsvPath, unigrams, bigrams, trigrams);

        System.out.println("\nExport CSV terminé : " + outputCsvPath);
    }
}
