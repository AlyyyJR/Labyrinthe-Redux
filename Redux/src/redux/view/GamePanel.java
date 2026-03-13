// FICHIER : GamePanel.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Cette classe gère l’affichage du jeu
//
// Rôle principal :
//   - dessiner le plateau (les cases du Board)
//   - dessiner la bille (Ball)
//   - afficher des overlays par-dessus :
//        * menu principal (Jouer / Choisir un niveau / Quitter)
//        * écran de sélection de niveau
//        * écran Game Over ("Appuie sur R...")
//        * écran de victoire ("Appuie sur ESPACE...")
//        * écran de fin ("BRAVO, TU AS FINI REDUX !")
//
// C’est la partie "Vue" de notre architecture MVC
// Elle ne contient pas la logique du jeu, uniquement le rendu graphique


package redux.view;

import redux.model.Ball;
import redux.model.Board;
import redux.model.ExitSquare;
import redux.model.GameState;
import redux.model.HoleSquare;
import redux.model.IceSquare;
import redux.model.SandSquare;
import redux.model.Square;
import redux.model.TeleporterSquare;
import redux.model.WallSquare;
import redux.model.BoostSquare;
import redux.model.SlowSquare;
import redux.model.ReverseSquare;

import javax.swing.*;
import java.awt.*;

/*
 * Composant Swing chargé d’afficher le plateau, la bille
 * et les différents écrans (menus, Game Over, victoire, fin)
 */
public class GamePanel extends JPanel {

    //===================================================
    // Attributs "modèle"
    //===================================================
    /* Plateau courant (grille de cases) */
    private Board board;
    /* Bille à afficher */
    private Ball ball;
    /* État global du jeu (win / game over) */
    private GameState state;
    /* Taille d’une case en pixels */
    private int cellSize = 40;
    /* Temps écoulé sur le niveau courant (en secondes) */
    private double elapsedTime = 0.0;

    //===================================================
    // Textures / sprites
    //===================================================
    /* Textures pour les différents types de cases */
    private Image imgGround;
    private Image imgWall;
    private Image imgIce;
    private Image imgSand;
    private Image imgTeleport;
    private Image imgTrap;
    private Image imgExit;
    private Image imgBoost;
    private Image imgSlow;
    private Image imgReverse;
    /* Texture de la bille */
    private Image imgBall;

    //===================================================
    // État d’affichage des menus / écrans
    //===================================================
    private boolean showMainMenu    = true;   // menu principal actif ?
    private boolean showLevelSelect = false;  // écran de sélection de niveau ?
    private boolean showEndScreen   = false;  // écran de fin (tous les niveaux terminés) ?
    // Infos pour le rendu du menu
    /* Index de l’option sélectionnée dans le menu principal (0, 1 ou 2) */
    private int menuIndex = 0;  // 0 = Jouer, 1 = Choisir niveau, 2 = Quitter
    /* Niveau actuellement surligné dans l’écran de sélection */
    private int selectedLevel = 1;
    /* Nombre maximum de niveaux disponibles */
    private int maxLevel = 1;

    //===================================================
    // Constructeur
    //===================================================
    /*
     * Initialise le panel graphique avec un plateau, une bille et un état de jeu
     * La taille du panel dépend des dimensions du Board
     * @param board plateau de jeu à afficher
     * @param ball  bille à dessiner
     * @param state état global du jeu (win / game over)
     */
    public GamePanel(Board board, Ball ball, GameState state) {
        this.board = board;
        this.ball  = ball;
        this.state = state;
        // dimension de la fenêtre en fonction du plateau
        setPreferredSize(new Dimension(
                board.getWidth() * cellSize,
                board.getHeight() * cellSize
        ));
        // Chargement des textures (dossier "images" à la racine du projet)
        loadTextures();
    }

    //===================================================
    // Chargement des textures
    //===================================================
    /*
     * Charge toutes les images nécessaires à partir du dossier "images"
     * Les fichiers attendus sont :
     *   src/images/Ground.png
     *   src/images/Wall.png
     *   src/images/Ice.png
     *   src/images/Sand.png
     *   src/images/Teleport.png
     *   src/images/Trap.png
     *   src/images/Exit.png
     *   src/images/Ball.png
     */
    private void loadTextures() {
        imgGround   = loadImage("src/images/Ground.png");
        imgWall     = loadImage("src/images/Wall.png");
        imgIce      = loadImage("src/images/Ice.png");
        imgSand     = loadImage("src/images/Sand.png");
        imgTeleport = loadImage("src/images/Teleport.png");
        imgTrap     = loadImage("src/images/Trap.png");
        imgExit     = loadImage("src/images/Exit.png");
        imgBall     = loadImage("src/images/Ball.png");   
        imgBoost    = loadImage("src/images/Boost.png");
        imgSlow     = loadImage("src/images/Slow.png");
        imgReverse  = loadImage("src/images/Reverse.png");
    }

    /* Méthode utilitaire pour charger une image */
    private Image loadImage(String path) {
        return new ImageIcon(path).getImage();
    }

    //===================================================
    // Méthode principale de dessin
    //===================================================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // On caste en Graphics2D pour activer l’antialiasing
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        int wPanel = getWidth();
        int hPanel = getHeight();
        //==========================
        // 1) Dessin des cases
        //==========================
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                Square sq = board.getSquare(i, j);
                int x = i * cellSize;
                int y = j * cellSize;
                Image sprite;
                // Choix de la texture en fonction du type de case
                if (sq instanceof WallSquare) {
                    sprite = imgWall;
                } else if (sq instanceof HoleSquare) {
                    sprite = imgTrap;
                } else if (sq instanceof ExitSquare) {
                    sprite = imgExit;
                } else if (sq instanceof IceSquare) {
                    sprite = imgIce;
                } else if (sq instanceof SandSquare) {
                    sprite = imgSand;
                } else if (sq instanceof BoostSquare) {
                    sprite = imgBoost;
                } else if (sq instanceof SlowSquare) {
                    sprite = imgSlow;
                } else if (sq instanceof ReverseSquare) {
                    sprite = imgReverse;
                } else if (sq instanceof TeleporterSquare) {
                    sprite = imgTeleport;
                } else {
                    sprite = imgGround;
                }
                // Dessin de la case avec sa texture
                g2.drawImage(sprite, x, y, cellSize, cellSize, null);
                // contour léger pour bien voir la grille
                //g2.setColor(new Color(0, 0, 0, 60));
                //g2.drawRect(x, y, cellSize, cellSize);
            }
        }

        //==========================
        // 2) Dessin de la bille
        //==========================
        int px = (int) ((ball.getX() - ball.getR()) * cellSize);
        int py = (int) ((ball.getY() - ball.getR()) * cellSize);
        int d  = (int) (2 * ball.getR() * cellSize);
        // Ombre sous la bille (petit effet de style)
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillOval(px + 1, py + 1, d - 3, d - 3);
        // Texture de la bille si dispo, sinon cercle bleu classique
        if (imgBall != null) {
            g2.drawImage(imgBall, px, py, d, d, null);
        } else {
            g2.setColor(Color.BLUE);
            g2.fillOval(px, py, d, d);
        }
        //==========================
        // 3) Overlays par-dessus
        //==========================
        // --- Menu principal ---
        if (showMainMenu) {
            drawMainMenu(g2, wPanel, hPanel);
            return; // on ne dessine pas les autres overlays par-dessus
        }
        // --- Écran de sélection de niveau ---
        if (showLevelSelect) {
            drawLevelSelect(g2, wPanel, hPanel);
            return;
        }
        // --- Écran de fin (bravo, tous les niveaux sont terminés) ---
        if (showEndScreen) {
            drawEndScreen(g2, wPanel, hPanel);
            return;
        }
        // --- OVERLAY GAME OVER ---
        if (state.isGameOver()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, wPanel, hPanel);
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(32f));
            g2.drawString("GAME OVER", wPanel / 2 - 100, hPanel / 2 - 20);
            g2.setFont(g2.getFont().deriveFont(18f));
            g2.drawString("Appuie sur R pour recommencer", wPanel / 2 - 150, hPanel / 2 + 20);
        }
        // --- OVERLAY WIN (niveau terminé) ---
        if (state.isWin()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, wPanel, hPanel);
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(32f));
            g2.drawString("LEVEL COMPLETED!", wPanel / 2 - 160, hPanel / 2 - 20);
            g2.setFont(g2.getFont().deriveFont(18f));
            g2.drawString("Appuie sur ESPACE pour continuer", wPanel / 2 - 175, hPanel / 2 + 20);
            // Affichage du temps final pour ce niveau
            String txt = String.format("Temps : %.1f s", elapsedTime);
            g2.drawString(txt, wPanel / 2 - 70, hPanel / 2 + 50);
        }
        //==========================
        // 4) Affichage du timer en jeu
        //==========================
        // On n’affiche le temps que pendant la partie
        if (!showMainMenu && !showLevelSelect && !showEndScreen && !state.isGameOver() && !state.isWin()) {
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(16f));
            String txt = String.format("Temps : %.1f s", elapsedTime);
            g2.drawString(txt, 10, 20);
        }
    }

    //===================================================
    // Dessin du menu principal
    //===================================================
    /*
     * Affiche le menu principal par-dessus le jeu
     */
    private void drawMainMenu(Graphics2D g2, int wPanel, int hPanel) {
        // fond semi-transparent
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, wPanel, hPanel);
        // titre du jeu
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(36f));
        g2.drawString("Labyrinthe", wPanel / 2 - 70, hPanel / 2 - 80);
        // options de menu
        g2.setFont(g2.getFont().deriveFont(20f));
        String[] options = {
                "1. Jouer",
                "2. Choisir un niveau",
                "3. Quitter"
        };
        int baseY = hPanel / 2 - 20;
        int lineHeight = 30;
        for (int i = 0; i < options.length; i++) {
            if (i == menuIndex) {
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.drawString(options[i], wPanel / 2 - 120, baseY + i * lineHeight);
        }
        // Aide clavier
        g2.setFont(g2.getFont().deriveFont(14f));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Utilise ↑ / ↓ pour naviguer, ENTREE pour valider", wPanel / 2 - 180, hPanel / 2 + 90);
    }

    //===================================================
    // Dessin de l’écran de sélection de niveau
    //===================================================
    /*
     * Affiche l’écran de sélection de niveau
     */
    private void drawLevelSelect(Graphics2D g2, int wPanel, int hPanel) {
        // fond semi-transparent
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, wPanel, hPanel);
        // titre
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(28f));
        g2.drawString("Sélection du niveau", wPanel / 2 - 140, hPanel / 2 - 60);
        // info niveau sélectionné
        g2.setFont(g2.getFont().deriveFont(22f));
        g2.drawString("Niveau : " + selectedLevel + " / " + maxLevel, wPanel / 2 - 110, hPanel / 2 - 10);
        // Aides clavier
        g2.setFont(g2.getFont().deriveFont(14f));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Utilise ← / → pour changer le niveau", wPanel / 2 - 150, hPanel / 2 + 30);
        g2.drawString("ENTREE pour lancer   |   ECHAP pour revenir au menu", wPanel / 2 - 200, hPanel / 2 + 50);
    }

    //===================================================
    // Dessin de l’écran de fin (dernier niveau terminé)
    //===================================================
    /*
     * Affiche l’écran de fin, lorsque tous les niveaux sont terminés
     */
    private void drawEndScreen(Graphics2D g2, int wPanel, int hPanel) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, wPanel, hPanel);
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(32f));
        g2.drawString("BRAVO, TU AS FINI LE LABYRINTHE !", wPanel / 2 - 220, hPanel / 2 - 40);
        g2.setFont(g2.getFont().deriveFont(18f));
        g2.drawString("ENTRÉE : retour au menu principal", wPanel / 2 - 170, hPanel / 2 + 10);
        g2.drawString("ÉCHAP : quitter le jeu", wPanel / 2 - 110, hPanel / 2 + 35);
    }

    //===================================================
    // Setters (utilisés par Game pour changer de niveau / menu)
    //===================================================
    /* Met à jour le plateau affiché (utile lors du changement de niveau) */
    public void setBoard(Board board) {
        this.board = board;
    }
    /* Met à jour la bille affichée (nouvelle bille sur nouveau niveau) */
    public void setBall(Ball ball) {
        this.ball = ball;
    }
    /* Met à jour l'état du jeu (win / game over) */
    public void setState(GameState state) {
        this.state = state;
    }
    /* Affiche ou cache le menu principal */
    public void setShowMainMenu(boolean showMainMenu) {
        this.showMainMenu = showMainMenu;
    }
    /* Affiche ou cache l’écran de sélection de niveau */
    public void setShowLevelSelect(boolean showLevelSelect) {
        this.showLevelSelect = showLevelSelect;
    }
    /* Affiche ou cache l’écran de fin */
    public void setShowEndScreen(boolean showEndScreen) {
        this.showEndScreen = showEndScreen;
    }
    /* Change l’option actuellement sélectionnée dans le menu principal */
    public void setMenuIndex(int menuIndex) {
        this.menuIndex = menuIndex;
    }
    /* Indique le niveau sélectionné dans l’écran de sélection de niveau */
    public void setSelectedLevel(int selectedLevel) {
        this.selectedLevel = selectedLevel;
    }
    /* Indique le nombre maximum de niveaux disponibles */
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }
    /* Met à jour le temps affiché (en secondes) */
    public void setElapsedTime(double t) {
        this.elapsedTime = t;
    }
}