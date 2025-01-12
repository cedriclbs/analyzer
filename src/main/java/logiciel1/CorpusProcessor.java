package logiciel1;

import java.util.Map;

/**
 * L'interface {@code CorpusProcessor} définit un contrat pour les classes
 * qui souhaitent analyser un texte et générer des statistiques sur les n-grammes.
 */
public interface CorpusProcessor {

    /**
     * Compte les occurrences des n-grammes dans une chaîne donnée.
     * Un n-gramme est une sous-chaîne de longueur {@code n}.
     *
     * @param c la chaîne de caractères à analyser
     * @param i la taille des n-grammes à extraire
     * @return une map associant chaque n-gramme à son nombre d'occurrences
     */
    public Map<String, Integer> countNGram(String c, int i);
}
