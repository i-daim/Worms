package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Bullet;
import upc.model.entites.projectiles.Projectile;

import upc.model.jeu.GameWorld;

public class Shotgun extends Weapon {

    public Shotgun() {
        super("Shotgun", 35, 8, 80);
    }

    @Override
    public void useItem(GameWorld gameWorld, Alien shooter) {
        int baseAngle = shooter.getAimAngle();
        int power = shooter.getShootPower();

        int startX = shooter.getPosition().getX() + 6;
        int shooterY = shooter.getPosition().getY();

        int[] spreads = {-12, -6, 0, 6, 12};
        for (int spread : spreads) {
            int startY = shooterY + spread / 3;
            Position startPos = new Position(startX, startY);
            int angle = baseAngle + spread;

            Projectile proj = new Bullet(this, shooter, startPos);
            gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);
        }
    }
}
