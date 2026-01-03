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

        double distance = 20.0;
        double rad = Math.toRadians(baseAngle);

        if (!shooter.getDirection()) { // Si regarde à gauche
            rad = Math.PI - rad;
        }

        int offsetX = (int) (Math.cos(rad) * distance);
        int offsetY = (int) (-Math.sin(rad) * distance);

        Position startPos = new Position(
                shooter.getPosition().getX() + offsetX,
                shooter.getPosition().getY() + offsetY - 10
        );


        int[] spreads = {-12, -6, 0, 6, 12};

        for (int spread : spreads) {
            int angle = baseAngle + spread;

            Projectile proj = new Bullet(this, shooter, startPos);
            gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);
        }
    }

    @Override
    public Type getType() {
        return Type.SHOTGUN;
    }
}
