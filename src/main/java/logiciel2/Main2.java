package logiciel2;

import logiciel1.*;
import java.util.List;
import java.util.Scanner;

/**
 * La classe {@code Main2} permet d'évaluer une disposition de clavier 
 * en calculant un score global basé sur un texte choisi et une disposition de clavier chargée depuis un fichier JSON.
 * 
 * Les étapes incluent :
 * 1. Chargement des dispositions de clavier depuis un fichier JSON.
 * 2. Sélection d'une disposition de clavier (par exemple, "FR" ou "EN").
 * 3. Lecture et analyse d'un fichier texte pour générer des n-grammes.
 * 4. Calcul du score d'évaluation pour la disposition sélectionnée.
 */
public class Main2 {

    /**
     * Méthode principale du programme d'évaluation des dispositions de clavier.
     * Elle guide l'utilisateur à travers les étapes suivantes :
     * <ul>
     *   <li>Chargement des dispositions de clavier depuis un fichier JSON.</li>
     *   <li>Sélection d'une disposition (par exemple, "FR" ou "EN").</li>
     *   <li>Lecture et analyse d'un fichier texte pour générer les unigrams, bigrams et trigrammes.</li>
     *   <li>Export des fréquences des n-grammes dans un fichier CSV.</li>
     *   <li>Calcul du score d'évaluation pour la disposition choisie.</li>
     * </ul>
     *
     * @param args arguments de la ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        // 1) Charge les dispositions de claviers depuis le JSON
        KeyboardsJson data = JsonLoader.loadKeyboards("config/keyboards.json");

        // 2) Demande à l'utilisateur de choisir un clavier
        Scanner sc = new Scanner(System.in);
        String userChoice;
        do {
            System.out.println("Choisissez un clavier (FR / EN) :\n");
            userChoice = sc.nextLine().trim().toUpperCase();

            if (!data.layouts.containsKey(userChoice)) {
                System.out.println("Clavier invalide ! Veuillez choisir entre 'FR' ou 'EN'.\n");
            }
        } while (!data.layouts.containsKey(userChoice));

        System.out.println("\nClavier choisi : " + userChoice);

        // Construire un KeyboardLayout pour le clavier choisi
        KeyboardLayout layout = new KeyboardLayout();
        List<KeyboardsJson.KeyDTO> chosenLayout = data.layouts.get(userChoice);
        for (KeyboardsJson.KeyDTO dto : chosenLayout) {
            Finger finger = Finger.valueOf(dto.finger);
            Hand hand = Hand.valueOf(dto.hand);
            char c = dto.character.charAt(0);
            layout.addKey(new Key(dto.row, dto.column, finger, hand, c));
        }

        // 3) Demander à l'utilisateur de choisir un texte
        String textChoice;
        String inputFilePath;
        do {
            System.out.println("\nChoisissez un texte à analyser :");
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
                    System.out.println("\nChoix de texte invalide ! Veuillez entrer un nombre entre 1 et 4.\n");
                    inputFilePath = null;
                }
            }
        } while (inputFilePath == null);

        // 4) Lecture du fichier sélectionné
        FileManagement fileManager = new FileReader();
        String corpusContent = fileManager.readFile(inputFilePath);

        // 5) Construire les nGram 
        CorpusAnalyzer analyzer = new CorpusAnalyzer();
        List<NGramFrequency> bigrams = analyzer.nGramList(corpusContent, 2);
        List<NGramFrequency> unigrams = analyzer.nGramList(corpusContent, 1);
        List<NGramFrequency> trigrams = analyzer.nGramList(corpusContent, 3);

        // Exporte les bigrams dans un fichier CSV
        FileExport exporter = new FileExport();
        String outputCsvPath = "output-ngram2.csv";
        exporter.exportToCsv(outputCsvPath, bigrams, unigrams, trigrams);

        System.out.println("\nLes fréquences des NGrammes ont été enregistrées dans : " + outputCsvPath);

        // 6) Configure l'évaluateur
        LayoutEvaluator evaluator = new LayoutEvaluator(
            1.0,  // weightSfb
            1.0,  // weightCiseau
            0.5,  // weightLsb
            2.0,  // weightRoulement
            1.5,  // weightAlternance
            1.2,  // weightRedirection
            2.0,  // weightMauvaiseRedirection
            1.0,  // weightSkipgram
            1.0   // weightFingerDistribution
        );

        // 7) Calcul le score
        double score = evaluator.evaluateLayout(unigrams, bigrams, trigrams, layout);
        System.out.println("\nScore global pour " + userChoice + " avec le texte choisi = " + score + "\n");
    }
}
