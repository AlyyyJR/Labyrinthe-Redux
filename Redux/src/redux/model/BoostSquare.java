// FICHIER : BoostSquare.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Case spéciale qui "booste" la bille
//
// Effet :
//   - Quand la bille ENTRE dans cette case, on multiplie
//     instantanément sa vitesse par un facteur (> 1)
//   - La case est traversable et n’empêche pas le mouvement
//   - Aucun effet sur leave() ou touch().
//
// Symbole associé dans les fichiers de niveau : 'B'

package redux.model;

/*
 * Case spéciale qui augmente instantanément la vitesse de la bille
 * Utilisée pour créer des zones rapides ou des défis de maîtrise
 */
public class BoostSquare extends Square {

    /*
     * La case est totalement traversable
     */
    @Override
    public boolean isEmpty() {
        return true;
    }
    /*
     * Effet principal : augmente la vitesse de la bille
     * @param b la bille entrant sur cette case
     */
    @Override
    public void enter(Ball b) {
        final double factor = 1.4; // boost de +40%
        b.setVx(b.getVx() * factor);
        b.setVy(b.getVy() * factor);
    }
    /*
     * Aucun effet en quittant la case
     */
    @Override
    public void leave(Ball b) { }
    /*
     * Aucun effet en simple contact (pas déclenché)
     */
    @Override
    public void touch(Ball b) { }
}