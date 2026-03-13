//===============================================
// FICHIER : IntegrationTest.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//-----------------------------------------------
// Tests d’intégration "fin de chaîne" : on vérifie
// le comportement de la bille sur un vrai Board
// chargé depuis un fichier de niveaux.
//
//   - testHoleOnBoard()
//       -> la bille est placée sur une case 'T'
//          dans levels/integration_hole.txt
//          -> GameState.gameOver doit passer à true
//
//   - testReverseOnBoard()
//       -> la bille est placée sur une case 'R'
//          dans levels/integration_reverse.txt
//          -> invertedControls doit passer à true,
//             puis revenir à false après leave()
//===============================================

package redux.test;

import redux.model.*;

public class IntegrationTest {

    private static int tests = 0;
    private static int ok    = 0;

    private static void assertTrue(boolean cond, String msg) {
        tests++;
        if (cond) ok++;
        else System.out.println("❌ " + msg);
    }

    private static void assertAlmostEquals(double expected, double actual, double eps, String msg) {
        tests++;
        if (Math.abs(expected - actual) <= eps) ok++;
        else System.out.println("❌ " + msg + " (attendu = " + expected + ", obtenu = " + actual + ")");
    }

    //===========================
    // main
    //===========================
    public static void main(String[] args) {
        System.out.println("=== Tests d’intégration Redux ===");

        testHoleOnBoard();
        testReverseOnBoard();

        System.out.println();
        System.out.println("Résultat global : " + ok + " / " + tests + " tests OK");
    }

    //===========================
    // 1) Trou sur un vrai niveau
    //===========================
    /**
     * Niveau utilisé : levels/integration_hole.txt
     *
     * #####
     * #...#
     * #.T.#
     * #...#
     * #####
     *
     * On place la bille au centre de la case 'T' (i=2,j=2)
     * donc en coordonnées réelles (2.5, 2.5),
     * puis on appelle enter() pour déclencher l’effet.
     */
    private static void testHoleOnBoard() {
        System.out.println("\n--- testHoleOnBoard ---");

        GameState state = new GameState();
        Board board = new Board("levels/integration_hole.txt", state);

        // bille au centre de la case (2,2) qui est un trou 'T'
        Ball b = new Ball(2.5, 2.5, 0.3);

        // 1) Vérifie déjà qu’on est bien sur un HoleSquare
        Square sq = board.getSquareAt(b.getX(), b.getY());
        assertTrue(sq instanceof HoleSquare,
                "La case (2,2) du niveau integration_hole.txt doit être un HoleSquare");

        // 2) On déclenche l'effet d'entrée dans la case
        sq.enter(b);

        // 3) Le GameState doit être en GameOver
        assertTrue(state.isGameOver(),
                "Après entrée dans la case trou, GameState.gameOver doit être true");
    }

    //===========================================
    // 2) ReverseSquare sur un vrai niveau
    //===========================================
    /**
     * Niveau utilisé : levels/integration_reverse.txt
     *
     * #####
     * #...#
     * #.R.#
     * #...#
     * #####
     *
     * On place la bille au centre de la case 'R', on appelle
     * enter() / leave() et on vérifie le flag invertedControls
     * ainsi que l’effet sur accelerate().
     */
    private static void testReverseOnBoard() {
        System.out.println("\n--- testReverseOnBoard ---");

        GameState state = new GameState();
        Board board = new Board("levels/integration_reverse.txt", state);

        Ball b = new Ball(2.5, 2.5, 0.3);

        // 1) Vérifie qu’on est bien sur une ReverseSquare
        Square sq = board.getSquareAt(b.getX(), b.getY());
        assertTrue(sq instanceof ReverseSquare,
                "La case (2,2) du niveau integration_reverse.txt doit être un ReverseSquare");

        ReverseSquare rSq = (ReverseSquare) sq;

        // 2) Avant d’entrer -> commandes normales
        assertTrue(!b.isInvertedControls(),
                "Avant enter(), invertedControls doit être false");

        // 3) On "entre" dans la case
        rSq.enter(b);
        assertTrue(b.isInvertedControls(),
                "Après enter(), invertedControls doit être true");

        // 4) On teste réellement l’inversion sur accelerate()
        b.setVx(0.0);
        b.setVy(0.0);
        b.accelerate(10.0, 0.0, 0.1); // commandes inversées -> vx ≈ -1
        assertAlmostEquals(-1.0, b.getVx(), 1e-6,
                "Avec commandes inversées, vx doit être ≈ -1.0 après accelerate(10,0,0.1)");

        // 5) On "quitte" la case : commandes redevenues normales
        rSq.leave(b);
        assertTrue(!b.isInvertedControls(),
                "Après leave(), invertedControls doit repasser à false");
    }
}