// FICHIER : BallTest.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Petite classe de tests "maison" pour vérifier
// le comportement de la classe Ball :
//
//   - accélération avec la souris
//   - frottement (arrêt / diminution)
//   - limitation de la vitesse
//
// On n’utilise pas JUnit : on affiche simplement
// le nombre de tests OK / total

package redux.test;

import redux.model.Ball;

public class BallTest {

    private static int tests = 0;
    private static int ok = 0;
    // Vérifie que deux valeurs sont presque égales
    private static void assertAlmostEquals(double expected, double actual, double eps, String msg) {
        tests++;
        if (Math.abs(expected - actual) <= eps) {
            ok++;
        } else {
            System.out.println("❌ " + msg + " (attendu = " + expected + ", obtenu = " + actual + ")");
        }
    }
    // Vérifie qu’une condition est vraie
    private static void assertTrue(boolean cond, String msg) {
        tests++;
        if (cond) {
            ok++;
        } else {
            System.out.println("❌ " + msg);
        }
    }

    //===========================
    // TEST 1 : accélération
    //===========================
    private static void testAccelerate() {
        Ball b = new Ball(0.0, 0.0, 0.3);
        // Déplacement souris : 10 en x, 0 en y, fa = 0.1
        b.accelerate(10, 0, 0.1);
        assertAlmostEquals(1.0, b.getVx(), 1e-6, "testAccelerate : vx invalide");
        assertAlmostEquals(0.0, b.getVy(), 1e-6, "testAccelerate : vy invalide");
    }

    //===========================
    // TEST 2 : frottement -> arrêt
    //===========================
    private static void testFrictionStop() {
        Ball b = new Ball(0.0, 0.0, 0.3);
        b.setVx(0.01);
        b.setVy(0.0);
        // f plus grand que la vitesse -> la bille doit s'arrêter
        b.applyFriction(0.02);
        assertAlmostEquals(0.0, b.getVx(), 1e-9, "testFrictionStop : vx devrait être 0");
        assertAlmostEquals(0.0, b.getVy(), 1e-9, "testFrictionStop : vy devrait être 0");
    }

    //===========================
    // TEST 3 : frottement -> diminution
    //===========================
    private static void testFrictionDecrease() {
        Ball b = new Ball(0.0, 0.0, 0.3);
        b.setVx(1.0);
        b.setVy(0.0);
        b.applyFriction(0.1); // v = 1, f = 0.1 -> factor = 0.9
        assertAlmostEquals(0.9, b.getVx(), 1e-6, "testFrictionDecrease : vx attendu ≈ 0.9");
        assertAlmostEquals(0.0, b.getVy(), 1e-6, "testFrictionDecrease : vy attendu = 0");
    }

    //===========================
    // TEST 4 : limitSpeed
    //===========================
    private static void testLimitSpeed() {
        Ball b = new Ball(0.0, 0.0, 0.3);
        b.setVx(1.0);
        b.setVy(1.0); // v = sqrt(2) ≈ 1.414
        b.limitSpeed(1.0);
        double v = Math.sqrt(b.getVx() * b.getVx() + b.getVy() * b.getVy());
        assertAlmostEquals(1.0, v, 1e-6, "testLimitSpeed : vitesse devrait être ≈ 1.0");
        assertTrue(v <= 1.0 + 1e-6, "testLimitSpeed : vitesse trop élevée");
    }

    public static void main(String[] args) {
        System.out.println("=== Tests Ball ===");
        testAccelerate();
        testFrictionStop();
        testFrictionDecrease();
        testLimitSpeed();
        System.out.println("Résultat : " + ok + " / " + tests + " tests OK");
    }
}