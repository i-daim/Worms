package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Bullet;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Projectile;

import upc.model.jeu.GameWorld;

public class Shotgun extends Weapon {
    public Shotgun() {
        super("Shotgun", 35, 8, 60);
    }

    @Override
    public void useItem(GameWorld gameWorld, Alien shooter) {
        int baseAngle = shooter.getAimAngle();
        int power = shooter.getShootPower();
        Position startPos = shooter.getPosition();
        int[] spreads = {-5, 0, 5};
        for (int spread : spreads) {
            Projectile proj = createProjectile(shooter, startPos, baseAngle + spread, power);
            gameWorld.getPhysicsEngine().addProjectile(proj, baseAngle + spread, power);
        }
    }
}
