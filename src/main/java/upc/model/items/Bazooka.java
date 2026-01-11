package upc.model.items;
import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;
import upc.model.entites.projectiles.Rocket;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Projectile;

public class Bazooka extends Weapon {


    /**Constantes représentants les dégats, le nombre de tir et la puissance de base*/
    private static final int DAMAGE = 45;
    private static final int NB_OF_SHOTS = 1;
    private static final int BASE_POWER = 20;

    /**
     * Constructeur pour {@code Bazooka}
     */
    public Bazooka() {
        super("Bazooka", DAMAGE, NB_OF_SHOTS, BASE_POWER);
    }

    /**
     * Méthode permettant l'utilisation de l'arme Bazooka
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
        double distance = 20.0;
        double rad = Math.toRadians(angle);

        if (!shooter.getDirection()) { // Si regarde à gauche
            rad = Math.PI - rad;
        }

        //Calcule le champ d'action de l'arme
        int offsetX = (int) (Math.cos(rad) * distance);
        int offsetY = (int) (-Math.sin(rad) * distance);

        //Position du projectile selon position du tireur + champ d'action de l'arme
        Position startPos = new Position(
                shooter.getPosition().getX() + offsetX,
                shooter.getPosition().getY() + offsetY - 5// Légèrement plus haut (épaules)
        );

        // Crée le projectile en lien avec l'arme en prenant en compte l'arme, le tireur
        // Et la position du tir
        Projectile proj = new Rocket(this, shooter, startPos);

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
        return Type.BAZOOKA;
    }
}
