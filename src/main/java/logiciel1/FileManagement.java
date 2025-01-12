package logiciel1;

import java.util.List;

/**
 * L'interface {@code FileManagement} définit les opérations nécessaires
 * pour gérer les fichiers, telles que la lecture et l'exportation de données.
 */
public interface FileManagement {

    /**
     * Lit le contenu d'un fichier spécifié par son chemin.
     *
     * @param filePath le chemin du fichier à lire
     * @return une chaîne contenant le contenu du fichier
     */
    public String readFile(String filePath);

    /**
     * Exporte les unigrams, bigrams et trigrams dans un fichier CSV.
     *
     * @param filePath le chemin complet du fichier CSV à créer
     * @param unigrams la liste des unigrams et leurs fréquences
     * @param bigrams la liste des bigrams et leurs fréquences
     * @param trigrams la liste des trigrams et leurs fréquences
     */
    public void exportToCsv(String filePath, List<NGramFrequency> unigrams, List<NGramFrequency> bigrams, List<NGramFrequency> trigrams);
}
