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
        
        // 2) Parser le CSV (bigrams) en une List<NGramFrequency> 
        //    Tu peux reprendre l’exemple de parse CSV ou écrire un parseur plus simple
        List<NGramFrequency> bigrams =frequencies;

        // 3) Pour l'exemple, on n'a pas de 1-gram, 3-gram => on simule des listes vides
        List<NGramFrequency> unigrams = List.of();
        List<NGramFrequency> trigrams = List.of();

        // 4) Construire un KeyboardLayout minimal
        KeyboardLayout layout = new KeyboardLayout();
        layout.addAllLetters();

        // 5) Construire l’évaluateur avec les poids souhaités
        LayoutEvaluator evaluator = new LayoutEvaluator(
            1.0,  // weightSfb
            1.0,  // weightCiseau
            0.5,  // weightLsb
            2.0,  // weightRoulement
            1.5,  // weightAlternance
            1.2,  // weightRedirection
            2.0,  // weightMauvaiseRedirection
            1.0   // weightSkipgram
        );

        // 6) Calculer le score
        double score = evaluator.evaluateLayout(unigrams, bigrams, trigrams, layout);
        System.out.println("Score global : " + score);

        // 7) Enregistrer le résultat dans un CSV (nomLayout, scoreLayout)
        FileManagement fileExport = new FileExport();
        fileExport.exportToCsv(
            "src/main/resources/output/layout-score.csv",
            List.of(new NGramFrequency("MyLayout", (int) score))
        );

    }
}
