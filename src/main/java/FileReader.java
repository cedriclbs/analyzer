package main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReader implements FileManagement {

    @Override
    public String readFile (String filePath){
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier : " + filePath, e);
        }
    }

    @Override
    public void exportToCsv(String filePath, List<NGramFrequency> frequencies) {
        throw new UnsupportedOperationException("Export non supporté dans FileReader");
    }
    
}