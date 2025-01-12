package logiciel2;

/**
 * L'énumération {@code MovementType} représente les différents types de mouvements
 * possibles entre les touches d'un clavier, que ce soit pour des bigrammes ou des trigrammes.
 * Ces mouvements sont utilisés pour évaluer l'ergonomie d'une disposition de clavier.
 */
public enum MovementType {
    SFB,
    LSB,
    CISEAU,
    ROULEMENT,
    ALTERNANCE,
    REDIRECTION,
    MAUVAISE_REDIRECTION,
    SKIPGRAM,
    UNKNOWN
}
