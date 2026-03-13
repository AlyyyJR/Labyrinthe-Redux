// FICHIER : SquareEffectTest.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Tests "maison" pour vérifier les effets des
// différentes cases spéciales sur la bille.
//
//   - HoleSquare    : gameOver = true
//   - ExitSquare    : win = true
//   - BoostSquare   : vitesse augmente
//   - SlowSquare    : vitesse diminue
//   - ReverseSquare : commandes inversées


package redux.test;

import redux.model.*;

public class SquareEffectTest {

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

    public static void main(String[] args) {
        System.out.println("=== Tests SquareEffect ===");
        testHoleSquare();
        testExitSquare();
        testBoostSquare();
        testSlowSquare();
        testReverseSquare();
        System.out.println("Résultat : " + ok + " / " + tests + " tests OK");
    }

    // ---------------------- TESTS --------------------------

    private static void testHoleSquare() {
        GameState state = new GameState();
        HoleSquare hole = new HoleSquare(state);
        Ball b = new Ball(1.0, 1.0, 0.3);
        hole.enter(b);
        assertTrue(state.isGameOver(), "HoleSquare.enter doit mettre gameOver = true");
    }

    private static void testExitSquare() {
        GameState state = new GameState();
        ExitSquare exit = new ExitSquare(state);
        Ball b = new Ball(1.0, 1.0, 0.3);
        exit.enter(b);
        assertTrue(state.isWin(), "ExitSquare.enter doit mettre win = true");
    }

    private static void testBoostSquare() {
        BoostSquare boost = new BoostSquare();
        Ball b = new Ball(0.0, 0.0, 0.3);
        b.setVx(1.0);
        b.setVy(0.0);
        double vAvant = Math.sqrt(b.getVx() * b.getVx() + b.getVy() * b.getVy());
        boost.enter(b);
        double vApres = Math.sqrt(b.getVx() * b.getVx() + b.getVy() * b.getVy());
        assertTrue(vApres > vAvant, "BoostSquare.enter doit AUGMENTER la vitesse");
    }

    private static void testSlowSquare() {
        SlowSquare slow = new SlowSquare();
        Ball b = new Ball(0.0, 0.0, 0.3);
        b.setVx(1.0);
        b.setVy(0.0);
        double vAvant = Math.sqrt(b.getVx() * b.getVx() + b.getVy() * b.getVy());
        slow.enter(b);
        double vApres = Math.sqrt(b.getVx() * b.getVx() + b.getVy() * b.getVy());
        assertTrue(vApres < vAvant, "SlowSquare.enter doit DIMINUER la vitesse");
    }

    private static void testReverseSquare() {
        ReverseSquare rev = new ReverseSquare();
        Ball b = new Ball(0.0, 0.0, 0.3);
        // Avant d’entrer dans la case, commandes normales
        b.setInvertedControls(false);
        rev.enter(b);
        assertTrue(b.isInvertedControls(),
                "ReverseSquare.enter doit activer les commandes inversées");
        // On teste que accelerate est bien inversé
        b.setVx(0.0);
        b.setVy(0.0);
        b.accelerate(10.0, 0.0, 0.1); // avec inversion -> vx ≈ -1.0
        assertAlmostEquals(-1.0, b.getVx(), 1e-6,
                "ReverseSquare : vx devrait être négatif après accelerate");
        // Quand on quitte la case, on revient à la normale
        rev.leave(b);
        assertTrue(!b.isInvertedControls(),
                "ReverseSquare.leave doit désactiver les commandes inversées");
    }
}