// FICHIER : Square.java
// Projet IPO : Redux
// Auteur : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Classe abstraite représentant une case du plateau
//
// Toutes les cases du jeu (mur, vide, trou, glace, sable,
// téléporteur, sortie, etc.) héritent de Square
//
// Les interactions possibles avec la bille sont :
//   • enter(b) : la bille ENTRE dans la case
//   • leave(b) : la bille QUITTE la case
//   • touch(b) : la bille TOUCHE la case sans y entrer
//
// Board ne connaît pas les types exacts : il manipule
// simplement des Square -> polymorphisme propre

package redux.model;

/*
 * Classe abstraite représentant une case générique du plateau
 */
public abstract class Square {

    /*
     * Indique si la case est traversable.
     * @return true si la bille peut entrer dans cette case, false si c’est un obstacle (ex : mur)
     */
    public abstract boolean isEmpty();
    /*
     * Appelé lorsque la bille entre totalement dans la case
     * Exemple : HoleSquare -> Game Over, ExitSquare -> victoire
     */
    public abstract void enter(Ball b);
    /*
     * Appelé lorsque la bille quitte la case
     * Utile pour les effets temporaires (ReverseSquare)
     */
    public abstract void leave(Ball b);
    /*
     * Appelé lorsque la bille touche la case sans y entrer
     * Exemple : un mur rebondit la balle (géré ailleurs)
     */
    public abstract void touch(Ball b);
}