// FICHIER : ExitSquare.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Case "sortie" du niveau
//
// Effet :
//   - Quand la bille entre dans cette case, le niveau est gagné.
//   - On déclenche l'état "win" dans GameState
//
// La sortie n'a pas d'effet particulier sur leave() ou touch()

package redux.model;

/*
 * Case représentant la sortie du labyrinthe
 * Quand la bille y entre, la partie est gagnée
 */
public class ExitSquare extends Square {
    /* Référence vers l'état global du jeu */
    private final GameState state;
    /*
     * Crée une case sortie liée à un GameState
     * @param state état global du jeu à modifier en cas de victoire
     */
    public ExitSquare(GameState state) {
        this.state = state;
    }
    /*
     * La sortie n'est pas considérée comme une case vide traversable.
     * @return false
     */
    @Override
    public boolean isEmpty() {
        // La bille entre dans la sortie, mais ce n'est pas une zone "neutre"
        return false;
    }
    /*
     * La bille atteint la sortie : on déclenche la victoire
     * @param b la bille qui entre dans la case
     */
    @Override
    public void enter(Ball b) {
        state.setWin(true);
    }
    /*
     * Aucun effet particulier en quittant la case
     */
    @Override
    public void leave(Ball b) { }
    /*
     * Aucun effet si la sortie est simplement touchée
     */
    @Override
    public void touch(Ball b) { }
}