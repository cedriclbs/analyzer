package main.java;

/**
 * Le record {@code NGramFrequency} représente un n-gramme et sa fréquence associée.
 * Il est immuable et fournit une structure simple pour stocker les n-grammes.
 *
 * @param nGram le n-gramme représenté sous forme de chaîne de caractères
 * @param frequency la fréquence du n-gramme dans un corpus donné
 */
public record NGramFrequency(String nGram, int frequency) {
}
