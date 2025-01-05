package main.java;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String inputFilePath = "src/main/resources/input/sample-corpus.txt";
        String outputFilePath = "src/main/resources/output/ngram-frequencies.csv";
        int n = 2;

        FileManagement fileManager = new FileReader();
        String corpusContent = fileManager.readFile(inputFilePath);

        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer();
        List<NGramFrequency> frequencies = corpusAnalyzer.nGramList(corpusContent, n);

        fileManager = new FileExport();
        fileManager.exportToCsv(outputFilePath, frequencies);

        System.out.println("Analyse terminée. Résultats enregistrés dans " + outputFilePath);

        // Parser le CSV (bigrams) en une List<NGramFrequency> 
        List<NGramFrequency> bigrams =frequencies;

        // Pour l'exemple, on n'a pas de 1-gram, 3-gram => on simule des listes vides
        List<NGramFrequency> unigrams = List.of();
        List<NGramFrequency> trigrams = List.of();

        // Construire un KeyboardLayout minimal
        KeyboardLayout layout = new KeyboardLayout();
        layout.addAllLetters();

        // Construire l’évaluateur avec les poids souhaités
        LayoutEvaluator evaluator = new LayoutEvaluator(1.0, 1.0, 0.5, 2.0,  1.5,  1.2,  2.0,  1.0);

        // Calculer le score
        double score = evaluator.evaluateLayout(unigrams, bigrams, trigrams, layout);
        System.out.println("Score global : " + score);

        // Enregistrer le résultat dans un CSV (nomLayout, scoreLayout)
        FileManagement fileExport = new FileExport();
        fileExport.exportToCsv(
            "src/main/resources/output/layout-score.csv",
            List.of(new NGramFrequency("MyLayout", (int) score))
        );

    }
}
