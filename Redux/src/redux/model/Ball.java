
// FICHIER : Ball.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Cette classe représente la bille contrôlée par le joueur
//
// La bille possède :
//   - une position (x, y) en coordonnées réelles
//   - une vitesse (vx, vy)
//   - un rayon r
//
// Elle peut :
//   - se déplacer (updatePosition)
//   - accélérer en fonction du mouvement de la souris (accelerate)
//   - subir un frottement (applyFriction)
//   - être limitée en vitesse maximale (limitSpeed)
//   - avoir les commandes inversées (invertedControls)
//   - avoir un cooldown de téléportation (teleportCooldown)
//
// Toute la physique du jeu est centrée sur cette classe


package redux.model;

/**
 * Représente la bille contrôlée par le joueur
 * Gère sa position, sa vitesse et quelques états spéciaux
 * (commandes inversées, cooldown de téléportation)
 */
public class Ball {

    //===================================================
    // Attributs : position, vitesse, rayon
    //===================================================
    /* Position réelle de la bille (coordonnées en cases) */
    private double x, y;
    /* Vitesse en x et en y */
    private double vx, vy;
    /* Rayon de la bille (en cases) */
    private double r;
    /* true -> les commandes de la souris sont inversées */
    private boolean invertedControls = false;
    /* Nombre de frames pendant lesquelles on ignore les téléporteurs */
    private int teleportCooldown = 0;

    //===================================================
    // Constructeur
    //===================================================
    /*
     * Initialise une bille à la position (x, y) avec un rayon r
     * La vitesse est mise à 0 au démarrage
     * @param x position initiale en x
     * @param y position initiale en y
     * @param r rayon de la bille
     */
    public Ball(double x, double y, double r) {
        this.x  = x;
        this.y  = y;
        this.r  = r;
        this.vx = 0.0;
        this.vy = 0.0;
    }

    //===================================================
    // Mise à jour de la position
    //===================================================
    /*
     * Met à jour la position en ajoutant la vitesse
     * Appelée à chaque frame dans la boucle de jeu
     */
    public void updatePosition() {
        x += vx;
        y += vy;
    }

    //===================================================
    // Frottement (comme indiqué dans le sujet)
    //===================================================
    /*
     * Applique un frottement f qui réduit progressivement la vitesse
     * Principe :
     *   - si la vitesse est très faible -> on arrête complètement la bille
     *   - sinon -> on réduit vx et vy proportionnellement
     * @param f coefficient de frottement (valeur positive)
     */
    public void applyFriction(double f) {
        double v = Math.sqrt(vx * vx + vy * vy); // norme de la vitesse
        if (v == 0) return;
        // Bille presque arrêtée -> on met vx = vy = 0
        if (v <= f) {
            vx = 0;
            vy = 0;
        }
        // Sinon -> diminution proportionnelle
        else {
            double factor = 1 - f / v;
            vx *= factor;
            vy *= factor;
        }
    }

    //===================================================
    // Accélération en fonction de la souris
    //===================================================
    /*
     * Accélère la bille selon le déplacement de la souris
     * Si invertedControls == true, on inverse la direction
     * @param sx déplacement horizontal de la souris
     * @param sy déplacement vertical de la souris
     * @param fa coefficient d’accélération
     */
    public void accelerate(double sx, double sy, double fa) {
        // Inversion des commandes si on est sur une ReverseSquare
        if (invertedControls) {
            sx = -sx;
            sy = -sy;
        }
        vx += fa * sx;
        vy += fa * sy;
    }

    //===================================================
    // Limitation de la vitesse
    //===================================================
    /*
     * Empêche la bille de dépasser une vitesse maximale.
     * Si la vitesse est supérieure à vmax, on la réduit mais
     * on conserve la direction
     * @param vmax vitesse maximale autorisée
     */
    public void limitSpeed(double vmax) {
        double v = Math.sqrt(vx * vx + vy * vy);
        if (v > vmax && v > 0) {
            double scale = vmax / v;
            vx *= scale;
            vy *= scale;
        }
    }

    //===================================================
    // Getters / Setters
    //===================================================
    public double getX() { return x; }
    public double getY() { return y; }
    public double getR() { return r; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public double getVx() { return vx; }
    public double getVy() { return vy; }
    public void setVx(double vx) { this.vx = vx; }
    public void setVy(double vy) { this.vy = vy; }

    // Flag pour les commandes inversées
    public boolean isInvertedControls() {
        return invertedControls;
    }
    public void setInvertedControls(boolean invertedControls) {
        this.invertedControls = invertedControls;
    }

    /*
     * Change directement la position de la bille
     * @param x nouvelle position en x
     * @param y nouvelle position en y
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //===================================================
    // Cooldown de téléportation
    //===================================================
    /*
     * @return nombre de frames restantes avant de pouvoir être re-téléporté
     */
    public int getTeleportCooldown() {
        return teleportCooldown;
    }
    /*
     * Fixe la valeur du cooldown de téléportation
     * @param teleportCooldown nombre de frames avant réactivation
     */
    public void setTeleportCooldown(int teleportCooldown) {
        this.teleportCooldown = teleportCooldown;
    }
    /*
     * À appeler à chaque frame pour décrémenter le cooldown si besoin
     */
    public void tickTeleportCooldown() {
        if (teleportCooldown > 0) {
            teleportCooldown--;
        }
    }
}