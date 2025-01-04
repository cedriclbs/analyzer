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

    }
}
