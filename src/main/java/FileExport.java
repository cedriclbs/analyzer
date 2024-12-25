package main.java;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileExport implements FileManagement {

    @Override
    public void exportToCsv(String filePath, List<NGramFrequency> frequencies) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("NGram, Frequency\n");

            for (NGramFrequency freq : frequencies) {
                writer.write(freq.nGram() + "," + freq.frequency() + "\n");
            }

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'écriture du fichier CSV : " + filePath, e);
        }

    }

    @Override
    public String readFile (String filePath){
        throw new UnsupportedOperationException("Export non supporté dans FileExport");
    }
 
   
}
