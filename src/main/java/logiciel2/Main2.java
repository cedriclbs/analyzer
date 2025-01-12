package logiciel2;

import logiciel1.CorpusAnalyzer;
import logiciel1.FileExport;
import logiciel1.FileReader;
import logiciel1.FileManagement;
import logiciel1.NGramFrequency;
import java.util.List;
import java.util.Scanner;

/**
 * La classe {@code Main2} est le point d'entrée du programme.
 * Elle permet de :
 * <ul>
 *   <li>Charger des configurations de claviers et de keymaps depuis des fichiers JSON.</li>
 *   <li>Lire un fichier texte et analyser son contenu pour générer des unigrams, bigrams et trigrams.</li>
 *   <li>Évaluer une disposition de clavier en fonction des fréquences de n-grammes.</li>
 *   <li>Exporter les n-grammes générés dans un fichier CSV.</li>
 * </ul>
 */
public class Main2 {
    /**
     * Point d'entrée principal du programme.
     * 
     * @param args les arguments passés en ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        //Charger keyboards.json
        KeyboardsJson kbJson = JsonLoader.loadKeyboards("config/keyboards.json");

        //Choisir FR ou EN
        Scanner sc = new Scanner(System.in);
        String layoutChoice;
        do {
            System.out.println("Choisissez un clavier (FR/EN): \n");
            layoutChoice = sc.nextLine().trim().toUpperCase();
        } while (!kbJson.layouts.containsKey(layoutChoice));

        //Construire ExtendedKeyboardLayout
        ExtendedKeyboardLayout extLayout = ExtendedKeyboardLayoutFactory.buildLayout(kbJson, layoutChoice);

        //Charger keymap.json
        KeymapJson keymap = KeymapJsonLoader.loadKeymap("config/keymap.json");

        //Lecture d'un fichier
        System.out.println("\nChoisissez un fichier texte :\n\n1) PoèmeFR1\n2) PoèmeFR2\n3) PoèmeENG1\n4) PoèmeENG2");
        String textChoice;
        String inputFilePath = null;
        do {
            System.out.print("\nVotre choix de 1 à 4 : ");
            textChoice = sc.nextLine().trim();
            switch (textChoice) {
                case "1" -> inputFilePath = "src/main/resources/input/sample-corpus1.txt";
                case "2" -> inputFilePath = "src/main/resources/input/sample-corpus2.txt";
                case "3" -> inputFilePath = "src/main/resources/input/sample-corpus3.txt";
                case "4" -> inputFilePath = "src/main/resources/input/sample-corpus4.txt";
                default -> {
                    System.out.println("\nChoix invalide!");
                    inputFilePath = null;
                }
            }
        } while (inputFilePath == null);

        // Lire le fichier
        FileManagement fm = new FileReader();
        String corpus = fm.readFile(inputFilePath);
        System.out.println("\nTexte lu depuis : " + inputFilePath+"\n");

        //Générer unigrams, bigrams, trigrams (caractères)
        CorpusAnalyzer analyzer = new CorpusAnalyzer();
        List<NGramFrequency> unigrams = analyzer.nGramList(corpus, 1);
        List<NGramFrequency> bigrams  = analyzer.nGramList(corpus, 2);
        List<NGramFrequency> trigrams = analyzer.nGramList(corpus, 3);

        //Éventuellement, exporter en CSV
        String outputFilePath = "src/main/resources/output/ngram-frequencies2.csv";
        FileExport exporter = new FileExport();
        exporter.exportToCsv(outputFilePath + "\n", bigrams, unigrams, trigrams);
        System.out.println("N-grammes exportés dans output-keymap-ngram.csv");

        //Évaluation via ExtendedLayoutEvaluator
        ExtendedLayoutEvaluator evaluator = new ExtendedLayoutEvaluator(
                1.0, // weightSfb
                1.0, // weightCiseau
                0.5, // weightLsb
                2.0, // weightRoulement
                1.5, // weightAlternance
                1.2, // weightRedirection
                2.0, // weightMauvaiseRedirection
                1.0  // weightSkipgram
        );
        double score = evaluator.evaluate(unigrams, bigrams, trigrams, extLayout, keymap);
        System.out.println("\nScore global avec le clavier " + layoutChoice + " = " + score + "\n");
    }
}
