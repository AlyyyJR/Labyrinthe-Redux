// FICHIER : LevelsLoadTest.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Tests pour vérifier le bon chargement de tous
// les fichiers de niveaux (level1.txt ... level8.txt)
//
// On vérifie pour chaque niveau :
//   - le fichier se charge sans exception
//   - largeur et hauteur > 0
//   - il existe au moins une case de sortie (ExitSquare)

package redux.test;

import redux.model.*;

public class LevelsLoadTest {

    private static int tests = 0;
    private static int ok    = 0;

    private static void assertTrue(boolean cond, String msg) {
        tests++;
        if (cond) ok++;
        else System.out.println("❌ " + msg);
    }

    public static void main(String[] args) {
        System.out.println("=== Tests LevelsLoad ===");
        int maxLevel = 8;   // idem que dans Game
        for (int level = 1; level <= maxLevel; level++) {
            testLevel(level);
        }
        System.out.println("Résultat : " + ok + " / " + tests + " tests OK");
    }

    private static void testLevel(int level) {
        String filename = "levels/level" + level + ".txt";
        System.out.println("\n--- Test niveau " + level + " (" + filename + ") ---");
        GameState state = new GameState();
        Board board = new Board(filename, state);
        int w = board.getWidth();
        int h = board.getHeight();
        assertTrue(w > 0 && h > 0,
                "Niveau " + level + " : largeur et hauteur doivent être > 0");
        // Au moins une case sortie ?
        boolean hasExit = false;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (board.getSquare(i, j) instanceof ExitSquare) {
                    hasExit = true;
                    break;
                }
            }
            if (hasExit) break;
        }
        assertTrue(hasExit,
                "Niveau " + level + " : doit contenir au moins une ExitSquare (S)");
    }
}