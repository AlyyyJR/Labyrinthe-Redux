// FICHIER : GameStateTest.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & YOUSSEF ABOU HASHISH
//==================================================================
// Tests simples pour vérifier le comportement de GameState :
//
//   - valeurs initiales
//   - setGameOver / setWin
//   - reset()


package redux.test;

import redux.model.GameState;

public class GameStateTest {

    private static int tests = 0;
    private static int ok = 0;

    private static void assertTrue(boolean cond, String msg) {
        tests++;
        if (cond) {
            ok++;
        } else {
            System.out.println("❌ " + msg);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Tests GameState ===");
        GameState state = new GameState();
        // état initial
        assertTrue(!state.isGameOver(), "Au début, gameOver devrait être false");
        assertTrue(!state.isWin(), "Au début, win devrait être false");
        // game over
        state.setGameOver(true);
        assertTrue(state.isGameOver(), "setGameOver(true) devrait mettre gameOver à true");
        // win
        state.setWin(true);
        assertTrue(state.isWin(), "setWin(true) devrait mettre win à true");
        // reset
        state.reset();
        assertTrue(!state.isGameOver(), "Après reset, gameOver devrait être false");
        assertTrue(!state.isWin(), "Après reset, win devrait être false");
        System.out.println("Résultat : " + ok + " / " + tests + " tests OK");
    }
}