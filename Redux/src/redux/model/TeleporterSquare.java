// FICHIER : TeleporterSquare.java
// Projet IPO : Redux
// Auteurs : Aly KONATE & Youssef ABOU HASHISH
//==================================================================
// Case téléporteur
//
// Les symboles 'A' et 'Z' dans les fichiers de niveaux
// créent deux TeleporterSquare connectés
//
// Quand la bille ENTRE dans la case :
//   -> elle est téléportée au centre de la case cible
//   -> un cooldown empêche la téléportation immédiate inverse
//
// L’anti-boucle est géré via teleportCooldown dans Ball


package redux.model;

/*
 * Case téléporteur : téléporte la bille vers une destination donnée
 */
public class TeleporterSquare extends Square {

    /* Coordonnées réelles (centre) de la destination */
    private double targetX;
    private double targetY;
    /*
     * Configure la destination du téléporteur
     *
     * @param x coordonnée x réelle
     * @param y coordonnée y réelle
     */
    public void setTarget(double x, double y) {
        this.targetX = x;
        this.targetY = y;
    }
    /*
     * Un téléporteur est une case traversable
     */
    @Override
    public boolean isEmpty() {
        return true;
    }
    /*
     * Téléporte la bille si elle n'est pas en cooldown
     */
    @Override
    public void enter(Ball b) {
        // 1) La bille vient d’être téléportée récemment → on ignore
        if (b.getTeleportCooldown() > 0) {
            return;
        }
        // 2) Téléportation vers la destination
        b.setX(targetX);
        b.setY(targetY);
        // 3) On coupe la vitesse pour éviter retéléportation / rebonds anormaux
        b.setVx(0);
        b.setVy(0);
        // 4) Cooldown (≈ 1 seconde à 50 FPS)
        b.setTeleportCooldown(50);
    }
    @Override
    public void leave(Ball b) {
        // Pas d’effet en sortant
    }
    @Override
    public void touch(Ball b) {
        // Pas d’effet sur simple contact
    }
}