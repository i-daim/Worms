package upc.model.items;

import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Bullet;
import upc.model.jeu.GameWorld;
import upc.model.entites.projectiles.Projectile;

public class Uzi extends Weapon {

    /**Constantes représentants les dégats, le nombre de tir et la puissance de base*/
    private static final int DAMAGE = 10;
    private static final int NB_OF_SHOTS = 30;
    private static final int BASE_POWER = 20;

    /**
     * Constructeur pour {@code Uzi}
     */
    public Uzi() {
        super("Uzi", DAMAGE, NB_OF_SHOTS, BASE_POWER);
    }

    /**
     * Méthode permettant l'utilisation de l'arme uzi
     * Crée le projectile dédié à l'arme dans le terrain de jeu
     * Et l'envoie à la position selon la visée du joueur
     * @param gameWorld : Le monde du jeu
     * @param shooter : le tireur
     * @return l'objet {@link Projectile} représentant le projectile de l'arme
     */
    @Override
    public Projectile useItem(GameWorld gameWorld, Alien shooter) {
        int angle = shooter.getAimAngle();
        int power = shooter.getShootPower();

        // Calcul de la position de départ (comme pour le Bazooka)
        double distance = 15.0; // Distance du corps (longueur du bras/arme)
        double rad = Math.toRadians(angle);

        if (!shooter.getDirection()) { // Si regarde à gauche
            rad = Math.PI - rad;
        }

        //Calcule le champ d'action de l'arme
        int offsetX = (int) (Math.cos(rad) * distance);
        int offsetY = (int) (-Math.sin(rad) * distance);

        // On ajuste la position : +offsetX, et on remonte de 10px pour être au niveau des épaules
        Position startPos = new Position(
                shooter.getPosition().getX() + offsetX,
                shooter.getPosition().getY() + offsetY - 6
        );

        // Crée le projectile en lien avec l'arme en prenant en compte l'arme, le tireur
        // Et la position du tir
        Projectile proj = new Bullet(this, shooter, startPos);

        //Ajout du projectile dans le moteur physique
        gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);

        //Renvoie le projectile
        return proj;
    }

    /**
     * Retourne le type d'item correspondant à l'arme
     * @return l'objet {@link Type} représentant l'item en lien avec l'arme
     */
    @Override
    public Type getType() {
        return Type.UZI;
    }
}
