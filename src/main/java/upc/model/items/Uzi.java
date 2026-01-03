package upc.model.items;

import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Bullet;
import upc.model.jeu.GameWorld;
import upc.model.entites.projectiles.Projectile;

public class Uzi extends Weapon {

    public Uzi() {
        super("Uzi", 10, 30, 80);
    }

    @Override
    public Projectile useItem(GameWorld gameWorld, Alien shooter) {
        int angle = shooter.getAimAngle();
        int power = shooter.getShootPower();

        // Calcul de la position de départ (comme pour le Bazooka)
        double distance = 15.0; // Distance du corps (longueur du bras/arme)
        double rad = Math.toRadians(angle);

        if (!shooter.getDirection()) { // Si regarde à gauche
            rad = Math.PI - rad;
        }

        int offsetX = (int) (Math.cos(rad) * distance);
        int offsetY = (int) (-Math.sin(rad) * distance);

        // On ajuste la position : +offsetX, et on remonte de 10px pour être au niveau des épaules
        Position startPos = new Position(
                shooter.getPosition().getX() + offsetX,
                shooter.getPosition().getY() + offsetY - 6
        );

        Projectile proj = new Bullet(this, shooter, startPos);
        gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);
        return proj;
    }

    @Override
    public Type getType() {
        return Type.UZI;
    }
}
