package upc.model.items;
import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;
import upc.model.entites.projectiles.Rocket;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Projectile;

public class Bazooka extends Weapon {

    private static final int DAMAGE = 45;
    private static final int NB_OF_SHOTS = 1;
    private static final int BASE_POWER = 20;

    public Bazooka() {
        super("Bazooka", DAMAGE, NB_OF_SHOTS, BASE_POWER);
    }

    @Override
    public Projectile useItem(GameWorld gameWorld, Alien shooter) {

        int angle = shooter.getAimAngle();
        int power = shooter.getShootPower();
        double distance = 20.0;
        double rad = Math.toRadians(angle);

        if (!shooter.getDirection()) { // Si regarde à gauche
            rad = Math.PI - rad;
        }

        int offsetX = (int) (Math.cos(rad) * distance);
        int offsetY = (int) (-Math.sin(rad) * distance);

        Position startPos = new Position(
                shooter.getPosition().getX() + offsetX,
                shooter.getPosition().getY() + offsetY - 5// Légèrement plus haut (épaules)
        );

        Projectile proj = new Rocket(this, shooter, startPos);
        gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);
        return proj;
    }

    @Override
    public Type getType() {
        return Type.BAZOOKA;
    }
}
