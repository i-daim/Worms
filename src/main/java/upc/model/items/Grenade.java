package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Throwable;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameWorld;

public class Grenade extends Weapon {

    public Grenade() {
        super("Grenade", 50, 3, 50);
    }

    public void useItem(GameWorld gameWorld, Alien shooter) {
        int angle = shooter.getAimAngle();
        int power = shooter.getShootPower();

        Projectile proj = new Throwable(this, shooter, 3);
        gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);
    }
}
