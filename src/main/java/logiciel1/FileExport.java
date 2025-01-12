package main.java;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * La classe {@code FileExport} implémente l'interface {@code FileManagement}
 * et fournit des fonctionnalités pour exporter des données sous forme de fichier CSV.
 */
public class FileExport implements FileManagement {

    /**
     * Exporte les unigrams, bigrams et trigrams dans un fichier CSV spécifié.
     *
     * @param filePath le chemin complet du fichier CSV à créer
     * @param unigrams la liste des unigrams et leurs fréquences
     * @param bigrams la liste des bigrams et leurs fréquences
     * @param trigrams la liste des trigrams et leurs fréquences
     * @throws RuntimeException si une erreur d'entrée/sortie survient lors de l'écriture du fichier
     */
    @Override
    public void exportToCsv(String filePath, List<NGramFrequency> unigrams, List<NGramFrequency> bigrams, List<NGramFrequency> trigrams) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("NGram, Frequency\n");
    
            // Écrire les unigrams
            writer.write("# Unigrams\n");
            for (NGramFrequency freq : unigrams) {
                writer.write(freq.nGram() + "," + freq.frequency() + "\n");
            }
    
            // Écrire les bigrams
            writer.write("\n# Bigrams\n");
            for (NGramFrequency freq : bigrams) {
                writer.write(freq.nGram() + "," + freq.frequency() + "\n");
            }
    
            // Écrire les trigrams
            writer.write("\n# Trigrams\n");
            for (NGramFrequency freq : trigrams) {
                writer.write(freq.nGram() + "," + freq.frequency() + "\n");
            }
    
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'écriture du fichier CSV : " + filePath, e);
        }
    }

    /**
     * Méthode non supportée pour la lecture de fichiers.
     * Cette classe est dédiée à l'exportation de fichiers uniquement.
     *
     * @param filePath le chemin du fichier à lire
     * @throws UnsupportedOperationException toujours levée, car cette opération n'est pas supportée
     */
    @Override
    public String readFile(String filePath) {
        throw new UnsupportedOperationException("Lecture de fichier non supportée dans FileExport");
    }
}
