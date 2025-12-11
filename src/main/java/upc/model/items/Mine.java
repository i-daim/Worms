package upc.model.items;
import upc.model.entites.Alien;

import upc.model.entites.projectiles.Trap;
import upc.model.jeu.GameWorld;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Projectile;

public class Mine extends Weapon {

    public static final int DAMAGE = 45;
    public static final int EXPLOSION_RADIUS = 3;
    public static final int ACTIVATION_RADIUS = 3;

    public Mine() {
        super("Mine", DAMAGE, 1, 15);
    }

    public void useItem(GameWorld gameWorld, Alien shooter) {
        int angle = shooter.getAimAngle();
        int power = shooter.getShootPower();
        Projectile mineProj = createProjectile(shooter, shooter.getPosition(), angle, power);
        gameWorld.getPhysicsEngine().addProjectile(mineProj, angle, power);
    }
    @Override
    public Projectile createProjectile(Alien shooter, Position startPosition, int angleDegrees,
                                       int powerPercent) {
        return new Trap(this, shooter, ACTIVATION_RADIUS, EXPLOSION_RADIUS);
    }
}
