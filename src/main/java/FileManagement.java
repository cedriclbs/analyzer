package main.java;

import java.util.List;

public interface FileManagement {
    public String readFile (String filePath);
    public void exportToCsv(String filePath, List<NGramFrequency> frequencies);
}