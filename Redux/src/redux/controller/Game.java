// FICHIER : Game.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Contrôleur principal du jeu
//
// Rôle de Game :
//   - initialiser le jeu (bille, plateau, fenêtre Swing)
//   - lancer la boucle de jeu (Timer Swing)
//   - gérer les entrées utilisateur :
//        • souris : contrôle de la bille
//        • clavier : menu, démarrer, changer de niveau, recommencer
//   - appliquer la physique globale :
//        • mise à jour de la position
//        • collisions via Board
//        • frottements (adaptés selon le type de case)
//        • limitation de la vitesse
//   - gérer les menus et l’écran de fin
//
// C’est la partie "Controller" de notre architecture MVC

package redux.controller;

import redux.model.*;
import redux.view.GamePanel;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Contrôleur principal de Redux : point d’entrée et boucle de jeu
 */
public class Game {

    //===================================================
    // Constantes physiques et paramètres du jeu
    //===================================================
    /* Rayon de la bille (en cases) */
    private static final double R = 0.4;
    /* Frottement de base */
    private static final double FRICTION = 0.001;
    /* Facteur d’accélération lié au mouvement de la souris */
    private static final double FA = 0.0005;
    /* Vitesse maximale de la bille.*/
    private static final double VMAX = 0.2;

    //===================================================
    // Attributs principaux du jeu
    //===================================================
    private GameState state;   // état global (win / game over)
    private Board board;       // plateau courant (niveau)
    private Ball ball;         // bille contrôlée par le joueur
    private GamePanel panel;   // vue Swing affichant le jeu

    /* Temps écoulé sur le niveau courant (en secondes) */
    private double elapsedTime = 0.0;
    // gestion multi-niveaux
    private int currentLevel = 1;
    private final int maxLevel = 9;
    // gestion de la souris (pour calculer le déplacement)
    private double lastMouseX, lastMouseY;
    private boolean firstMouse = true;
    // indique si la partie est en cours (physique active)
    private boolean started = false;
    // gestion des menus
    private boolean inMainMenu    = true;
    private boolean inLevelSelect = false;
    private boolean inEndScreen   = false;  // vrai si on affiche l’écran de fin
    private int menuIndex         = 0;      // 0 = Jouer, 1 = Choisir niveau, 2 = Quitter
    private int levelChoice       = 1;      // niveau surligné dans l’écran de sélection
    // commandes inversées (case R)
    private boolean reverseControls = false;

    //===================================================
    // Méthode main : point d'entrée du programme
    //===================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game().start());
    }

    //===================================================
    // Initialisation de la fenêtre et des objets du jeu
    //===================================================
    /*
     * Initialise le premier niveau, crée la fenêtre Swing
     * et installe les souris / clavier et la boucle de jeu
     */
    private void start() {
        // 1) Création de l’état, du plateau et de la bille
        state = new GameState();
        board = new Board("levels/level1.txt", state);
        ball  = new Ball(1.5, 1.5, R);  // départ en haut à gauche
        // 2) Création du panel graphique (Vue)
        panel = new GamePanel(board, ball, state);
        panel.setMaxLevel(maxLevel);
        panel.setMenuIndex(menuIndex);
        panel.setSelectedLevel(levelChoice);
        panel.setShowMainMenu(true);      // on commence sur le menu principal
        panel.setShowLevelSelect(false);
        panel.setShowEndScreen(false);    // écran de fin caché au début
        // Initialisation du timer d’affichage
        elapsedTime = 0.0;
        panel.setElapsedTime(0.0);
        // 3) Fenêtre principale
        JFrame frame = new JFrame("Labyrinthe - multi-level");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //=============================
        // Souris : contrôle de la bille
        //=============================
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMove(e, ball);
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseMove(e, ball);
            }
        });

        //=============================
        // Clavier : menus / niveaux / restart / écran de fin
        //=============================
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                //--- 0) Écran de fin : priorité absolue ---
                if (inEndScreen) {
                    handleEndScreenKeys(key);
                    return;
                }
                //--- 1) D’abord, gestion des menus ---
                if (inMainMenu) {
                    handleMainMenuKeys(key);
                    return;
                }
                if (inLevelSelect) {
                    handleLevelSelectKeys(key);
                    return;
                }
                //--- 2) Puis, gestion du jeu en cours ---
                // ESPACE : niveau suivant si Win
                if (state.isWin() && key == KeyEvent.VK_SPACE) {
                    if (currentLevel < maxLevel) {
                        currentLevel++;
                        loadLevel(currentLevel);
                    } else {
                        // Dernier niveau terminé -> écran de fin
                        inEndScreen = true;
                        started     = false;
                        panel.setShowEndScreen(true);
                    }
                }
                // R : recommencer le niveau si Game Over
                if (state.isGameOver() && key == KeyEvent.VK_R) {
                    loadLevel(currentLevel);
                }
            }
        });

        //=============================
        // Boucle de jeu (Timer Swing)
        //=============================
        int delay = 20; // ~50 FPS
        new Timer(delay, e -> {
            // On ne met à jour la physique que si le jeu est lancé
            // et qu’on n’est ni en Game Over, ni en Win, ni sur l’écran de fin
            if (started && !state.isGameOver() && !state.isWin() && !inEndScreen) {
                // Gestion du cooldown de téléportation
                ball.tickTeleportCooldown();
                // Mise à jour du temps
                elapsedTime += delay / 1000.0;   // 20 ms -> 0,02 s
                panel.setElapsedTime(elapsedTime);
                // 1) Mise à jour de la position de la bille
                ball.updatePosition();
                // 2) Gestion des collisions avec le plateau
                board.handleCollisions(ball);
                // 3) Effets de case + frottement adapté
                Square sq = board.getSquareAt(ball.getX(), ball.getY());
                reverseControls = (sq instanceof ReverseSquare);
                // --- Friction par défaut ---
                double friction = FRICTION;
                // Glace
                if (sq instanceof IceSquare) {
                    friction = FRICTION * 0.05;
                }
                // Sable
                else if (sq instanceof SandSquare) {
                    friction = FRICTION * 8.0;
                }
                // Boost
                else if (sq instanceof BoostSquare) {
                    friction = FRICTION * 0.3;   // glisse + accélère
                }
                // Slow
                else if (sq instanceof SlowSquare) {
                    friction = FRICTION * 12.0;  // ralentissement très fort
                }
                // Appliquer friction + limite vitesse
                ball.applyFriction(friction);
                ball.limitSpeed(VMAX);
            }
            // 4) Redessiner le jeu (cases + bille + overlays)
            panel.repaint();
        }).start();
    }

    //===================================================
    // Gestion du déplacement de la souris
    //===================================================
    /*
     * Calcule le déplacement de la souris entre deux événements
     * et en déduit l’accélération à appliquer à la bille
     */
    private void handleMouseMove(MouseEvent e, Ball ball) {
        double mx = e.getX();
        double my = e.getY();
        // Si on est dans un menu ou sur l’écran de fin, on ignore la souris
        if (inMainMenu || inLevelSelect || inEndScreen) {
            lastMouseX = mx;
            lastMouseY = my;
            firstMouse = true;
            return;
        }
        // Première position de la souris -> initialisation
        if (firstMouse) {
            lastMouseX = mx;
            lastMouseY = my;
            firstMouse = false;
            return;
        }
        // Déplacement de la souris (en pixels)
        double sx = mx - lastMouseX;
        double sy = my - lastMouseY;
        // Accélération de la bille dans la direction du mouvement de la souris.
        // Par défaut, l'accélération est positive (contrôles normaux). Et si la bille se trouve sur une case ReverseSquare, on inverse le signe du facteur d'accélération afin d'inverser les commandes
        double factor = FA;
        if (reverseControls) {
            factor *= -1;
        }
        ball.accelerate(sx, sy, factor);
        ball.limitSpeed(VMAX);
        // Mise à jour de la dernière position connue
        lastMouseX = mx;
        lastMouseY = my;
    }

    //===================================================
    // Gestion des touches dans le menu principal
    //===================================================
    /*
     * Navigation et validation dans le menu principal.
     */
    private void handleMainMenuKeys(int key) {
        int nbOptions = 3; // Jouer / Choisir niveau / Quitter
        if (key == KeyEvent.VK_UP) {
            menuIndex = (menuIndex - 1 + nbOptions) % nbOptions;
            panel.setMenuIndex(menuIndex);
        } else if (key == KeyEvent.VK_DOWN) {
            menuIndex = (menuIndex + 1) % nbOptions;
            panel.setMenuIndex(menuIndex);
        } else if (key == KeyEvent.VK_ENTER) {
            // Valider l’option sélectionnée
            switch (menuIndex) {
                case 0: // Jouer
                    inMainMenu  = false;
                    inEndScreen = false;
                    started     = true;
                    panel.setShowMainMenu(false);
                    panel.setShowEndScreen(false);
                    break;
                case 1: // Choisir un niveau
                    inMainMenu    = false;
                    inLevelSelect = true;
                    inEndScreen   = false;
                    started       = false;
                    panel.setShowMainMenu(false);
                    panel.setShowEndScreen(false);
                    panel.setShowLevelSelect(true);
                    panel.setSelectedLevel(levelChoice);
                    break;
                case 2: // Quitter
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }

    //===================================================
    // Gestion des touches dans l’écran de sélection
    //===================================================
    /*
     * Gestion du menu de sélection de niveau.
     */
    private void handleLevelSelectKeys(int key) {
        if (key == KeyEvent.VK_LEFT) {
            if (levelChoice > 1) {
                levelChoice--;
                panel.setSelectedLevel(levelChoice);
            }
        } else if (key == KeyEvent.VK_RIGHT) {
            if (levelChoice < maxLevel) {
                levelChoice++;
                panel.setSelectedLevel(levelChoice);
            }
        } else if (key == KeyEvent.VK_ENTER) {
            // Lancer le niveau choisi
            currentLevel   = levelChoice;
            inLevelSelect  = false;
            inEndScreen    = false;
            started        = true;
            panel.setShowLevelSelect(false);
            panel.setShowEndScreen(false);
            loadLevel(currentLevel);
        } else if (key == KeyEvent.VK_ESCAPE) {
            // Retour au menu principal
            inLevelSelect = false;
            inMainMenu    = true;
            inEndScreen   = false;
            started       = false;
            panel.setShowLevelSelect(false);
            panel.setShowMainMenu(true);
            panel.setShowEndScreen(false);
        }
    }

    //===================================================
    // Gestion des touches sur l’écran de fin
    //===================================================
    /*
     * Gestion de l’écran de fin (après le dernier niveau)
     *  - ENTREE : retour au menu principal et reset sur le niveau 1
     *  - ECHAP  : quitter le jeu
     */
    private void handleEndScreenKeys(int key) {
        if (key == KeyEvent.VK_ENTER) {
            // retour au menu principal + reset niveau 1
            inEndScreen  = false;
            started      = false;
            inMainMenu   = true;
            currentLevel = 1;
            loadLevel(currentLevel);
            panel.setShowEndScreen(false);
            panel.setShowMainMenu(true);
        } else if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    //===================================================
    // Chargement d’un niveau (restart / niveau choisi)
    //===================================================
    /*
     * Charge un nouveau niveau à partir de son numéro
     * Réinitialise l'état, le plateau, la bille et met à jour la vue
     * @param level numéro du niveau à charger (1..maxLevel)
     */
    private void loadLevel(int level) {
        // Remise à zéro du timer pour le nouveau niveau
        elapsedTime = 0.0;
        panel.setElapsedTime(0.0);
        // Réinitialisation de l’état global
        state.reset();
        String filename = "levels/level" + level + ".txt";
        // Nouveau plateau et nouvelle bille
        board = new Board(filename, state);
        ball  = new Ball(1.5, 1.5, R);
        ball.setTeleportCooldown(0);   // remet à zéro par sécurité
        // Mise à jour de la vue
        panel.setBoard(board);
        panel.setBall(ball);
        panel.setState(state);
        // Réinitialisation des infos de contrôle
        firstMouse = true;
        // Les flags de menus ne sont PAS forcés ici :
        // ils sont gérés là où loadLevel est appelé
    }
}