package upc.model.items;

import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;
import upc.model.entites.projectiles.Bullet;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Projectile;

public class Uzi extends Weapon {
    public Uzi() {
        super("Uzi", 10, 30, 80);
    }

    public void useItem(GameWorld gameWorld, Alien shooter) {
        int angle = shooter.getAimAngle();
        int power = shooter.getShootPower(); // Souvent fixe pour les balles, ou max

        Projectile proj = createProjectile(shooter, shooter.getPosition(), angle, power);
        gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);
    }

    @Override
    public Projectile createProjectile(Alien shooter, Position startPosition, int angleDegrees, int powerPercent) {
        return new Bullet(this, shooter);
    }

}
