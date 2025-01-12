package main.java;

/**
 * Décrit une touche physique du clavier + le caractère associé.
 * row/column : indices ou positions pour détecter ciseau, etc.
 */
public record Key(int row, int column, Finger finger, Hand hand, char character) { 
    
}
