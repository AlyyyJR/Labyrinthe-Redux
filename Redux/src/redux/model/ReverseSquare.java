// FICHIER : ReverseSquare.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Case spéciale qui inverse les commandes du joueur
//
// Tant que la bille est sur cette case, les déplacements
// de la souris sont appliqués dans le sens opposé
//
//   - enter() : active l’inversion
//   - leave() : désactive l’inversion
//
// Symbole dans les fichiers de niveau : 'R'

package redux.model;

/*
 * Case inversant les commandes du joueur tant que la bille
 * se trouve dessus
 */
public class ReverseSquare extends Square {

    /*
     * Case traversable
     * @return true
     */
    @Override
    public boolean isEmpty() {
        return true;
    }
    /*
     * La bille entre sur la case → on active les commandes inversées
     * @param b bille qui entre sur la case
     */
    @Override
    public void enter(Ball b) {
        b.setInvertedControls(true);
    }
    /*
     * La bille quitte la case → on rétablit les commandes normales
     * @param b bille qui quitte la case
     */
    @Override
    public void leave(Ball b) {
        b.setInvertedControls(false);
    }
    /*
     * Aucun effet sur simple contact
     */
    @Override
    public void touch(Ball b) { }
}