// FICHIER : SlowSquare.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Case spéciale qui ralentit fortement la bille
//
// Effet :
//   - traversable
//   - quand la bille ENTRE dans cette case, sa vitesse est
//     immédiatement réduite (effet "frein")
//
// Symbole dans les fichiers de niveau : 'L'

package redux.model;

/*
 * Case qui applique un freinage instantané à la bille
 * lorsqu'elle y entre
 */
public class SlowSquare extends Square {

    /*
     * La case est traversable
     * @return true
     */
    @Override
    public boolean isEmpty() {
        return true;
    }
    /*
     * La bille entre sur un slow → on diminue sa vitesse
     * @param b bille entrant sur la case
     */
    @Override
    public void enter(Ball b) {
        double factor = 0.4; // baisse de 60 %
        b.setVx(b.getVx() * factor);
        b.setVy(b.getVy() * factor);
    }
    /*
     * Effet instantané : rien à faire en sortant
     */
    @Override
    public void leave(Ball b) { }
    /*
     * Aucun effet spécifique sur simple contact
     */
    @Override
    public void touch(Ball b) { }
}