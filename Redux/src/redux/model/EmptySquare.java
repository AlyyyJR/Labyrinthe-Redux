// FICHIER : EmptySquare.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Case vide du plateau
//
// Comportement :
//   - traversable
//   - aucun effet sur la bille (pas de rebond)
//   - aucune modification de vitesse ou de direction
//
// C’est la case par défaut du labyrinthe
// Symbole associé : '.'


package redux.model;

/*
 * Représente une case complètement neutre du plateau.
 * Elle est traversable et ne modifie jamais l’état de la bille
 */
public class EmptySquare extends Square {

    /*
     * Une case vide ne bloque pas la bille
     * @return toujours true
     */
    @Override
    public boolean isEmpty() {
        return true;
    }
    /*
     * Aucun effet lorsque la bille entre dans la case
     */
    @Override
    public void enter(Ball b) { }
    /*
     * Aucun effet lorsque la bille quitte la case
     */
    @Override
    public void leave(Ball b) { }
    /*
     * Aucun effet sur simple contact
     */
    @Override
    public void touch(Ball b) { }
}