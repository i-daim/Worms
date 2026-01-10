package upc.model.items;
import upc.model.entites.Alien;

import upc.model.entites.projectiles.Trap;
import upc.model.jeu.GameWorld;
import upc.model.entites.projectiles.Projectile;

public class Mine extends Weapon {

    /**
     * Constantes représentants les dégats, le nombre de tir, la puissance de base,
     * le champ d'action de l'explosion et d'activation
     */
    public static final int DAMAGE = 45;
    public static final int NB_OF_SHOTS = 1;
    public static final int BASE_POWER = 5;
    public static final int EXPLOSION_RADIUS = 30;
    public static final int ACTIVATION_RADIUS = 20;

    /**
     * Constructeur pour {@code Mine}
     */
    public Mine() {
        super("Mine", DAMAGE, NB_OF_SHOTS, BASE_POWER);
    }


    /**
     * Méthode permettant l'utilisation de l'arme mine
     * Crée le projectile dédié à l'arme dans le terrain de jeu
     * @param gameWorld : Le monde du jeu
     * @param shooter : le tireur
     * @return l'objet {@link Projectile} représentant le projectile de l'arme
     */
    @Override
    public Projectile useItem(GameWorld gameWorld, Alien shooter) {
        int angle = shooter.getAimAngle();
        int power = shooter.getShootPower();

        // Crée le projectile en lien avec l'arme
        Projectile mineProj = createProjectile(shooter);

        //Ajout du projectile dans le moteur physique
        gameWorld.getPhysicsEngine().addProjectile(mineProj, angle, power);

        //Renvoie le projectile
        return mineProj;
    }


    /**
     * Méthode de créer le projectile de l'arme mine
     * @param shooter : le tireur
     * @return l'objet {@link Projectile} représentant le projectile de l'arme
     */
    public Projectile createProjectile(Alien shooter) {

        // Crée le projectile en lien avec l'arme en prenant en compte l'arme, le tireur
        // Et le champ d'action de l'explosion et d'activation
        return new Trap(this, shooter, ACTIVATION_RADIUS, EXPLOSION_RADIUS);
    }

    /**
     * Retourne le type d'item correspondant à l'arme
     * @return l'objet {@link Type} représentant l'item en lien avec l'arme
     */
    @Override
    public Type getType() {
        return Type.MINE;
    }
}
