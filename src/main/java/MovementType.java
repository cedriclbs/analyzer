package main.java;

/**
 * Les types de mouvements (bigrammes/trigrammes) qu'on veut détecter.
 */
public enum MovementType {
    SFB,                  // Single-Finger Bigram
    LSB,                  // Lateral Stretch Bigram
    CISEAU,               // ciseau
    ROULEMENT,            // roulement (rolling)
    ALTERNANCE,           // main gauche -> main droite ou l'inverse
    REDIRECTION,          // changement de direction (main unique)
    MAUVAISE_REDIRECTION, // redirection "pire" (sans index, par ex.)
    SKIPGRAM,             // skipgram (same finger, main unique, mais séparé par l'autre main)
    UNKNOWN               // inconnu
}
