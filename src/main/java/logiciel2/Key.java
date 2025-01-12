package logiciel2;

/**
 * Le record {@code Key} représente une touche physique d'un clavier, 
 * ainsi que les informations associées telles que sa position, le doigt utilisé, 
 * la main et le caractère correspondant.
 *
 * @param row l'indice de la rangée où se trouve la touche sur le clavier
 * @param column l'indice de la colonne où se trouve la touche sur le clavier
 * @param finger le doigt utilisé pour cette touche (voir {@link Finger})
 * @param hand la main utilisée pour cette touche (voir {@link Hand})
 * @param character le caractère associé à cette touche
 */
public record Key(int row, int column, Finger finger, Hand hand, char character) {

}
