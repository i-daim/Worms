package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Bullet;
import upc.model.entites.projectiles.Projectile;

import upc.model.jeu.GameWorld;

public class Shotgun extends Weapon {

    /**Constantes représentants les dégats, le nombre de tir et la puissance de base*/
    private static final int DAMAGE = 15;
    private static final int NB_OF_SHOTS = 2;
    private static final int BASE_POWER = 20;

    /**
     * Constructeur pour {@code Shotgun}
     */
    public Shotgun() {
        super("Shotgun", DAMAGE, NB_OF_SHOTS, BASE_POWER);
    }


    /**
     * Méthode permettant l'utilisation de l'arme shotgun
     * Crée le projectile dédié à l'arme dans le terrain de jeu
     * Et l'envoie à la position selon la visée du joueur
     * @param gameWorld : Le monde du jeu
     * @param shooter : le tireur
     * @return l'objet {@link Projectile} représentant le projectile de l'arme
     */
    @Override
    public Projectile useItem(GameWorld gameWorld, Alien shooter) {
        int baseAngle = shooter.getAimAngle();
        int power = shooter.getShootPower();

        double distance = 20.0;
        double rad = Math.toRadians(baseAngle);

        if (!shooter.getDirection()) { // Si regarde à gauche
            rad = Math.PI - rad;
        }

        //Calcule le champ d'action de l'arme
        int offsetX = (int) (Math.cos(rad) * distance);
        int offsetY = (int) (-Math.sin(rad) * distance);

        //Position du projectile selon position du tireur + champ d'action de l'arme
        Position startPos = new Position(
                shooter.getPosition().getX() + offsetX,
                shooter.getPosition().getY() + offsetY - 10// Légèrement plus haut (épaules)
        );


        //Tableau représenant la dispersion entre chaque balle
        int[] spreads = {-12, -6, 0, 6, 12};

        Projectile proj = null;

        for (int spread : spreads) {

            //Angle changeant selon la dispersion choisi pour la balle
            int angle = baseAngle + spread;

            // Crée le projectile en lien avec l'arme en prenant en compte l'arme, le tireur
            // Et la position du tir
            proj = new Bullet(this, shooter, startPos);

            //Ajout du projectile dans le moteur physique
            gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);
        }

        //Renvoie le projectile
        return proj;
    }

    /**
     * Retourne le type d'item correspondant à l'arme
     * @return l'objet {@link Type} représentant l'item en lien avec l'arme
     */
    @Override
    public Type getType() {
        return Type.SHOTGUN;
    }
}
