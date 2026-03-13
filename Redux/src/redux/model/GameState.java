// FICHIER : GameState.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// État global d'une partie
//
// Il mémorise :
//   - si le joueur a perdu (gameOver)
//   - si le joueur a gagné (win)
//
// Game et GamePanel s'appuient dessus pour :
//   - afficher l'écran Game Over ou Victoire
//   - bloquer la physique quand la partie est terminée

package redux.model;

/*
 * Représente l'état global d'une partie :
 * victoire, défaite, ou en cours
 */
public class GameState {

    /**Vrai si la partie est perdue (bille tombée dans un trou) */
    private boolean gameOver = false;
    /* Vrai si la partie est gagnée (bille arrivée à la sortie) */
    private boolean win = false;

    //===================================================
    // Lecture de l'état
    //===================================================
    /*
     * @return true si la partie est perdue
     */
    public boolean isGameOver() {
        return gameOver;
    }
    /*
     * @return true si la partie est gagnée
     */
    public boolean isWin() {
        return win;
    }

    //===================================================
    // Modification de l'état
    //===================================================
    /*
     * Change l'état "Game Over"
     * @param gameOver true si la partie doit être considérée comme perdue
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    /*
     * Change l'état "Win"
     * @param win true si la partie doit être considérée comme gagnée
     */
    public void setWin(boolean win) {
        this.win = win;
    }

    //===================================================
    // Réinitialisation
    //===================================================
    /*
     * Réinitialise l'état du jeu (utilisé lors d'un restart / changement de niveau)
     */
    public void reset() {
        gameOver = false;
        win = false;
    }
}