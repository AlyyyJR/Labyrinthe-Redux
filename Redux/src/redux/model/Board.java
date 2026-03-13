// FICHIER : Board.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Cette classe représente le plateau de jeu
//
// Rôle principal :
//   - stocker les cases (Square) dans une grille 2D
//   - charger un niveau depuis un fichier texte
//   - fournir des informations sur les cases (getSquare, getSquareAt)
//   - gérer les collisions entre la bille et :
//        * les bords du plateau
//        * les murs internes (WallSquare)
//        * les cases spéciales (trou, sortie, glace, sable, etc.)
//        * les téléporteurs A / Z
//
// C’est ici que l’on fait le lien entre la physique de la bille
// et la structure du labyrinthe (murs, trous, sortie, téléporteurs…)

package redux.model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/*
 * Représente le plateau : une grille de Square
 * Gère le chargement des niveaux et les collisions
 * entre la bille et le labyrinthe
 */
public class Board {

    //===================================================
    // Attributs
    //===================================================
    /* Grille des cases du plateau */
    private Square[][] grid;
    /* Dimensions du plateau (en nombre de cases) */
    private int width, height;
    /* État global du jeu (win / game over) */
    private GameState state;
    // Téléporteurs (une paire A / Z)
    private TeleporterSquare teleporterA = null;
    private TeleporterSquare teleporterZ = null;
    private int teleporterAX, teleporterAY;
    private int teleporterZX, teleporterZY;

    //===================================================
    // Constructeur
    //===================================================
    /*
     * Construit un plateau en lisant un fichier texte
     * @param filename nom du fichier de niveau (ex : "levels/level1.txt")
     * @param state    référence vers l'état du jeu, pour les cases spéciales
     */
    public Board(String filename, GameState state) {
        this.state = state;
        loadFromFile(filename);
    }

    //===================================================
    // Chargement du plateau depuis un fichier texte
    //===================================================
    /*
     * Lit un fichier texte et remplit la grille de Square.
     * Chaque caractère correspond à un type de case :
     *   - '#' : mur                   -> WallSquare
     *   - '.' : vide                  -> EmptySquare
     *   - 'T' : trou                  -> HoleSquare
     *   - 'E' : sortie                -> ExitSquare
     *   - 'I' : glace                 -> IceSquare
     *   - 'S' : sable                 -> SandSquare
     *   - 'B' : boost de vitesse      -> BoostSquare
     *   - 'L' : ralentissement fort   -> SlowSquare
     *   - 'R' : commandes inversées   -> ReverseSquare
     *   - 'A' / 'Z' : téléporteurs    -> TeleporterSquare (paire A <-> Z)
     *
     */
    private void loadFromFile(String filename) {
        List<String> lines = new ArrayList<>();
        // Lecture ligne par ligne
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // on ignore les lignes vides éventuelles à la fin
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur : impossible de lire " + filename);
        }
        // Hauteur = nombre de lignes
        height = lines.size();
        // Largeur = longueur MAX parmi toutes les lignes
        width = 0;
        for (String l : lines) {
            if (l.length() > width) {
                width = l.length();
            }
        }
        grid = new Square[width][height];
        // Remplissage de la grille à partir des caractères
        for (int j = 0; j < height; j++) {
            String line = lines.get(j);
            for (int i = 0; i < width; i++) {
                // Si la ligne est trop courte, on complète avec un mur
                char c;
                if (i < line.length()) {
                    c = line.charAt(i);
                } else {
                    c = '#';   // ou '.' si tu préfères des cases vides
                }
                switch (c) {
                    case '#':
                        grid[i][j] = new WallSquare();
                        break;
                    case '.':
                        grid[i][j] = new EmptySquare();
                        break;
                    case 'T':
                        grid[i][j] = new HoleSquare(state);
                        break;
                    case 'E':
                        grid[i][j] = new ExitSquare(state);
                        break;
                    case 'I':
                        grid[i][j] = new IceSquare();
                        break;
                    case 'S':
                        grid[i][j] = new SandSquare();
                        break;
                    case 'B':
                        grid[i][j] = new BoostSquare();
                        break;
                    case 'L':
                        grid[i][j] = new SlowSquare();
                        break;
                    case 'R':
                        grid[i][j] = new ReverseSquare();
                        break;
                    case 'A': {
                        TeleporterSquare ta = new TeleporterSquare();
                        grid[i][j] = ta;
                        teleporterA  = ta;
                        teleporterAX = i;
                        teleporterAY = j;
                        break;
                    }
                    case 'Z': {
                        TeleporterSquare tb = new TeleporterSquare();
                        grid[i][j] = tb;
                        teleporterZ  = tb;
                        teleporterZX = i;
                        teleporterZY = j;
                        break;
                    }
                    default:
                        grid[i][j] = new EmptySquare();
                }
            }
        }
        // Connexion de la paire de téléporteurs A <-> Z, si elle existe
        if (teleporterA != null && teleporterZ != null) {
            double centerAX = teleporterAX + 0.5;
            double centerAY = teleporterAY + 0.5;
            double centerZX = teleporterZX + 0.5;
            double centerZY = teleporterZY + 0.5;
            teleporterA.setTarget(centerZX, centerZY);
            teleporterZ.setTarget(centerAX, centerAY);
        }
    }

    //===================================================
    // Accesseurs sur les dimensions et les cases
    //===================================================
    /* @return largeur du plateau (en cases) */
    public int getWidth()  {
        return width;
    }
    /* @return hauteur du plateau (en cases) */
    public int getHeight() {
        return height;
    }
    /*
     * Retourne la case à la position (i, j) dans la grille
     * @param i indice de colonne (0 <= i < width)
     * @param j indice de ligne    (0 <= j < height)
     * @return la case correspondante
     */
    public Square getSquare(int i, int j) {
        return grid[i][j];
    }
    /*
     * Retourne la case correspondant aux coordonnées réelles (x, y)
     * de la bille, en convertissant en indices de grille
     * @param x coordonnée réelle en x
     * @param y coordonnée réelle en y
     * @return la case correspondante, ou null si en dehors du plateau
     */
    public Square getSquareAt(double x, double y) {
        int i = (int) Math.floor(x);
        int j = (int) Math.floor(y);
        if (i < 0 || i >= width || j < 0 || j >= height) return null;
        return grid[i][j];
    }

    //===================================================
    // Gestion de la collision avec une case particulière
    //===================================================
    /*
     * Vérifie et gère la collision entre la bille et la case
     * située autour du point (px, py)
     * - Si c'est un mur, on calcule un rebond (réflexion de la vitesse)
     * - Ensuite, on appelle sq.enter(b) pour gérer les cases spéciales (trou, sortie, téléporteur, etc.)
     * @param b  bille
     * @param px abscisse du point de contact
     * @param py ordonnée du point de contact
     */
    private void checkCaseCollision(Ball b, double px, double py) {
        int i = (int) Math.floor(px);
        int j = (int) Math.floor(py);
        // en dehors du plateau -> rien à faire
        if (i < 0 || i >= width || j < 0 || j >= height) return;
        Square sq = grid[i][j];
        // Cas particulier : mur -> rebond
        if (sq instanceof WallSquare) {
            // Vecteur centre de la case -> centre de la bille
            double dx = b.getX() - i - 0.5;
            double dy = b.getY() - j - 0.5;
            // Normalisation de la normale
            double norm = Math.sqrt(dx * dx + dy * dy);
            if (norm == 0) return;
            dx /= norm;
            dy /= norm;
            // Projection de la vitesse sur la normale
            double vdotn = b.getVx() * dx + b.getVy() * dy;
            // Si la bille va vers le mur -> on calcule la réflexion
            if (vdotn < 0) {
                b.setVx(b.getVx() - 2 * vdotn * dx);
                b.setVy(b.getVy() - 2 * vdotn * dy);
            }
        }
        // Interaction avec la case (trou, sortie, téléporteur, etc.)
        sq.enter(b);
    }

    //===================================================
    // Gestion globale des collisions bille / plateau
    //===================================================
    /*
     * Gère toutes les collisions possibles pour une bille :
     * 1) Collisions avec les bords du plateau :
     *    - rebond si la bille sort du rectangle [0, width] x [0, height]
     * 2) Collisions avec les cases du labyrinthe :
     *    - on regarde les côtés de la bille (gauche, droite, haut, bas)
     *    - puis les 4 coins
     *    - pour chaque point, on appelle checkCaseCollision
     * @param b bille à tester
     */
    public void handleCollisions(Ball b) {
        double x = b.getX();
        double y = b.getY();
        double r = b.getR();
        double vx = b.getVx();
        double vy = b.getVy();
        // 1) Collisions avec les bords du plateau
        if (x - r < 0 && vx < 0) {
            b.setVx(-vx);
        }
        if (x + r > width && vx > 0) {
            b.setVx(-vx);
        }
        if (y - r < 0 && vy < 0) {
            b.setVy(-vy);
        }
        if (y + r > height && vy > 0) {
            b.setVy(-vy);
        }
        // 2) Collisions avec les murs / cases spéciales du labyrinthe
        //    -> on teste les côtés de la bille
        checkCaseCollision(b, x - r, y);     // côté gauche
        checkCaseCollision(b, x + r, y);     // côté droit
        checkCaseCollision(b, x, y - r);     // côté haut
        checkCaseCollision(b, x, y + r);     // côté bas
        //    -> puis les 4 coins de la bille
        checkCaseCollision(b, x - r, y - r);
        checkCaseCollision(b, x + r, y - r);
        checkCaseCollision(b, x - r, y + r);
        checkCaseCollision(b, x + r, y + r);
    }
}