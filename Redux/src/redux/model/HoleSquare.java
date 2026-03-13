// FICHIER : HoleSquare.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Case "trou" dans le plateau
//
// Effet :
//   - si la bille entre dans cette case, la partie est perdue
//   - on déclenche l'état "Game Over" dans GameState
//
// Aucun effet sur leave() ou touch() : tout se joue au enter()

package redux.model;

/*
 * Case représentant un trou dans le labyrinthe
 * Si la bille y entre, la partie est immédiatement perdue
 */
public class HoleSquare extends Square {

    /* Référence vers l'état global du jeu */
    private final GameState state;
    /*
     * Crée un trou lié à un GameState
     * @param state état global du jeu à modifier en cas de défaite
     */
    public HoleSquare(GameState state) {
        this.state = state;
    }
    /*
     * Un trou n'est pas traversable comme une case vide
     * @return false
     */
    @Override
    public boolean isEmpty() {
        return false;
    }
    /*
     * La bille tombe dans le trou : partie perdue
     * @param b la bille entrant dans le trou
     */
    @Override
    public void enter(Ball b) {
        state.setGameOver(true);
    }
    /*
     * Quitter un trou n'a pas vraiment de sens → aucun effet
     */
    @Override
    public void leave(Ball b) { }
    /*
     * Aucun effet sur simple contact
     */
    @Override
    public void touch(Ball b) { }
}