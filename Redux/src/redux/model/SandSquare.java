// FICHIER : SandSquare.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Case « sable » (ou sol lourd)
//
// Effet :
//   - traversable
//   - la bille est ralentie car le frottement est plus fort
//
// Comme pour la glace, l'effet principal est géré dans Game.java :
//   → on augmente la friction lorsque la bille se trouve
//     sur une SandSquare

package redux.model;

/**
 * Case de sable : le frottement est augmenté lorsque la bille
 * roule dessus, ce qui la ralentit
 */
public class SandSquare extends Square {

    /*
     * Le sable est traversable (ce n'est pas un mur)
     * @return true
     */
    @Override
    public boolean isEmpty() {
        return true;
    }
    /*
     * La bille entre sur la case sable → pas d'action immédiate ici
     * Le ralentissement est géré dans la physique globale (Game)
     */
    @Override
    public void enter(Ball b) { }
    /*
     * La bille quitte la case sable → aucun effet
     */
    @Override
    public void leave(Ball b) { }
    /*
     * Contact de la bille avec la case → aucun effet direct
     */
    @Override
    public void touch(Ball b) { }
}