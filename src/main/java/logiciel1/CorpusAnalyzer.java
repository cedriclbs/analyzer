package main.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * La classe {@code CorpusAnalyzer} est une implémentation de {@code CorpusProcessor}
 * permettant d'analyser un texte et de générer des statistiques sur les n-grammes.
 */
public class CorpusAnalyzer implements CorpusProcessor {

    /**
     * Compte les occurrences des n-grammes dans une chaîne donnée.
     * Un n-gramme est une sous-chaîne de longueur {@code n}.
     *
     * @param contenu la chaîne de caractères à analyser
     * @param n la taille des n-grammes à extraire
     * @return une map associant chaque n-gramme à son nombre d'occurrences
     */
    @Override
    public Map<String, Integer> countNGram(String contenu, int n) {
        contenu = contenu.replaceAll("\\s+", "").trim(); // Supprime les espaces et nettoie la chaîne

        Map<String, Integer> freq = new HashMap<>();

        for (int i = 0; i <= contenu.length() - n; i++) {
            String ngram = contenu.substring(i, i + n); // Extrait le n-gramme
            freq.put(ngram, freq.getOrDefault(ngram, 0) + 1); // Met à jour la fréquence
        }

        return freq;
    }

    /**
     * Convertit la map contenant les n-grammes et leurs fréquences
     * en une liste d'objets {@code NGramFrequency} pour garantir l'immuabilité.
     *
     * @param contenu la chaîne de caractères à analyser
     * @param n la taille des n-grammes à extraire
     * @return une liste d'objets {@code NGramFrequency} représentant les n-grammes et leurs fréquences
     */
    public List<NGramFrequency> nGramList(String contenu, int n) {
        Map<String, Integer> corpusProcessed = countNGram(contenu, n); // Génère la map des fréquences
        return corpusProcessed
                .entrySet() // Obtient les paires clé-valeur de la map
                .stream()
                .map(entry -> new NGramFrequency(entry.getKey(), entry.getValue())) // Convertit chaque paire en objet NGramFrequency
                .collect(Collectors.toList()); // Collecte les objets sous forme de liste
    }
}
