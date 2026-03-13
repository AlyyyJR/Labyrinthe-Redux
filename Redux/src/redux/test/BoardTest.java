// FICHIER : BoardTest.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & YOUSSEF ABOU HASHISH
//==================================================================
// Tests pour vérifier quelques aspects de Board :
//
//   - chargement d’un niveau depuis "levels/level1.txt"
//   - dimensions cohérentes
//   - bordures en murs (si le niveau est construit comme ça)
//   - cohérence entre getSquare(i,j) et getSquareAt(x,y)
//
// ⚠ Ces tests supposent que level1.txt est entouré de murs (‘#’).
//   Si ce n’est pas le cas, il faudra adapter les assertions
//   sur les bords

package redux.test;

import redux.model.*;

public class BoardTest {

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
        System.out.println("=== Tests Board ===");
        GameState state = new GameState();
        Board board = new Board("levels/level1.txt", state);
        int w = board.getWidth();
        int h = board.getHeight();
        // dimensions raisonnables
        assertTrue(w > 0 && h > 0, "Dimensions du board doivent être > 0");
        // Bord haut et bas : murs (si level1 est entouré de '#')
        for (int i = 0; i < w; i++) {
            assertTrue(board.getSquare(i, 0) instanceof WallSquare,
                    "Bord haut case " + i + " devrait être un mur");
            assertTrue(board.getSquare(i, h - 1) instanceof WallSquare,
                    "Bord bas case " + i + " devrait être un mur");
        }
        // Bord gauche et droit : murs
        for (int j = 0; j < h; j++) {
            assertTrue(board.getSquare(0, j) instanceof WallSquare,
                    "Bord gauche case " + j + " devrait être un mur");
            assertTrue(board.getSquare(w - 1, j) instanceof WallSquare,
                    "Bord droit case " + j + " devrait être un mur");
        }
        // Test de correspondance getSquareAt avec getSquare
        Square s1 = board.getSquare(1, 1);
        Square s2 = board.getSquareAt(1.2, 1.7);
        assertTrue(s1 == s2,
                "getSquareAt(1.2,1.7) devrait renvoyer la même case que getSquare(1,1)");

        System.out.println("Résultat : " + ok + " / " + tests + " tests OK");
    }
}