package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Throwable;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameWorld;

public class Grenade extends Weapon {

    private static final int DAMAGE = 50;
    private static final int RADIUS = 30;

    public Grenade() {
        super("Grenade", DAMAGE, 1, 50);
    }

    @Override
    public void useItem(GameWorld gameWorld, Alien shooter) {
        int angle = shooter.getAimAngle();
        int power = shooter.getShootPower();

        Projectile proj = new Throwable(this, shooter, 3, RADIUS);
        gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);
    }

    @Override
    public Type getType() {
        return Type.GRENADE;
    }
}
