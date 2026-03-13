// FICHIER : WallSquare.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Mur du labyrinthe
//
// Le mur n’est PAS traversable :
//   • enter() ne devrait jamais être déclenché
//   • les rebonds sont entièrement gérés dans
//       Board.handleCollisions()
//
// Cette classe est volontairement minimale :
// toute la logique de collision est centralisée dans Board

package redux.model;

/*
 * Case mur : obstacle infranchissable
 */
public class WallSquare extends Square {

    /*
     * Un mur est un obstacle solide
     */
    @Override
    public boolean isEmpty() {
        return false;
    }
    /*
     * La bille ne devrait jamais entrer dans un mur
     * Le rebond est géré avant dans Board.handleCollisions()
     */
    @Override
    public void enter(Ball b) { }
    /* Quitter un mur -> aucun effet */
    @Override
    public void leave(Ball b) { }
    /*
     * Toucher un mur -> rebond géré dans Board, donc rien à faire ici
     */
    @Override
    public void touch(Ball b) { }
}