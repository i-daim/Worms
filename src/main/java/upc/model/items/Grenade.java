package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Throwable;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameWorld;

public class Grenade extends Weapon {

    /**
     * Constantes représentants les dégats, le nombre de tir, la puissance de base
     * et le champ d'action
     */
    private static final int DAMAGE = 50;
    private static final int NB_OF_SHOTS = 1;
    private static final int BASE_POWER = 10;
    private static final int RADIUS = 30;

    /**
     * Constructeur pour {@code Grenade}
     */
    public Grenade() {
        super("Grenade", DAMAGE, NB_OF_SHOTS, BASE_POWER);
    }

    /**
     * Méthode permettant l'utilisation de l'arme grenade
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

        // Crée le projectile en lien avec l'arme en prenant en compte l'arme, le tireur,
        // le temps d'action et le champ d'action
        Projectile proj = new Throwable(this, shooter, 3, RADIUS);

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
        return Type.GRENADE;
    }
}
