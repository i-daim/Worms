package upc.model.items;
import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;
import upc.model.entites.projectiles.Rocket;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Projectile;

public class Bazooka extends Weapon {
    public Bazooka() {
        super("Bazooka", 45, 3, 70);
    }
    @Override
    public void useItem(GameWorld gameWorld, Alien shooter) {
        int angle = shooter.getAimAngle();
        int power = shooter.getShootPower();
        Position startPos = shooter.getPosition();
        Projectile proj = createProjectile(shooter, startPos, angle, power);
        gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);

    }
    @Override
    public Projectile createProjectile(Alien shooter, Position startPosition, int angleDegrees,
                                       int powerPercent) {
        return new Rocket(this, shooter);
    }

}
