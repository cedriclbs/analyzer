package main.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CorpusAnalyzer implements CorpusProcessor {

    @Override
    //Fonction qui compte les nGrammes en fonction de n, et qui les mets dans une map.
    public Map<String, Integer> countNGram(String contenu, int n) {
        contenu = contenu.replaceAll("\\s+", "").trim();

        Map<String, Integer> freq = new HashMap<>();
        
        for (int i = 0 ; i <= contenu.length()-n ; i++){
            String ngram = contenu.substring(i, i+n);
            freq.put(ngram, freq.getOrDefault(ngram, 0) + 1);
        }

        return freq;
    }



    //Fonction qui convertit la map contenant les nGrammes en une liste de NGramFrequency : question d'immuabilité
    public List<NGramFrequency> nGramList(String contenu, int n) {
        Map<String, Integer> corpusProcessed = countNGram(contenu, n);
        return corpusProcessed
            .entrySet() //ensemble des paires clé-valeur de la map
            .stream()
            .map(entry-> new NGramFrequency(entry.getKey(), entry.getValue())) //transfotme ("ab", 2) -> new NGramFrequency("ab", 2) -> immuable
            .collect(Collectors.toList());
    }

}