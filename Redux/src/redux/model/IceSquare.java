// FICHIER : IceSquare.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Case « glace »
//
// Effet :
//   - traversable
//   - réduit fortement le frottement → la bille glisse longtemps
//
// L'effet principal n'est pas codé ici directement :
//   → dans Game.java, on diminue la friction si la bille
//     se trouve sur une IceSquare

package redux.model;

/*
 * Case de glace : la bille glisse davantage car le frottement
 * global est réduit lorsque cette case est sous la bille
 */
public class IceSquare extends Square {

    /*
     * La glace est une case traversable
     * @return true
     */
    @Override
    public boolean isEmpty() {
        return true;
    }
    /*
     * La bille entre sur la glace → aucun effet direct ici.
     * Le glissement est géré dans la physique globale (Game)
     */
    @Override
    public void enter(Ball b) { }
    /*
     * La bille quitte la glace → aucun effet
     */
    @Override
    public void leave(Ball b) { }
    /*
     * Contact de la bille avec la glace → aucun effet direct
     */
    @Override
    public void touch(Ball b) { }
}