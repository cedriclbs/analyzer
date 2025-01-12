package logiciel1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * La classe {@code FileReader} implémente l'interface {@code FileManagement}
 * et fournit une fonctionnalité pour lire le contenu des fichiers texte.
 * Cette classe ne prend pas en charge l'exportation de fichiers CSV.
 */
public class FileReader implements FileManagement {

    /**
     * Lit le contenu d'un fichier spécifié par son chemin.
     *
     * @param filePath le chemin du fichier à lire
     * @return une chaîne contenant le contenu du fichier
     * @throws RuntimeException si une erreur d'entrée/sortie survient lors de la lecture
     */
    @Override
    public String readFile(String filePath) {
        try {
            return Files.readString(Path.of(filePath)); // Lit le contenu du fichier
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier : " + filePath, e);
        }
    }

    /**
     * Méthode non supportée pour l'exportation de fichiers CSV.
     * Cette classe est exclusivement dédiée à la lecture de fichiers.
     *
     * @param filePath le chemin du fichier CSV à exporter
     * @param unigrams la liste des unigrams et leurs fréquences
     * @param bigrams la liste des bigrams et leurs fréquences
     * @param trigrams la liste des trigrams et leurs fréquences
     * @throws UnsupportedOperationException toujours levée, car cette opération n'est pas supportée
     */
    @Override
    public void exportToCsv(String filePath, List<NGramFrequency> unigrams, List<NGramFrequency> bigrams, List<NGramFrequency> trigrams) {
        throw new UnsupportedOperationException("Export non supporté dans FileReader");
    }
}
