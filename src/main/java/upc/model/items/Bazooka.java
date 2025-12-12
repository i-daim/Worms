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
        System.out.println("🎯 useItem appelée");
        int angle = shooter.getAimAngle();
        int power = shooter.getShootPower();
        double distance = 25.0;
        double rad = Math.toRadians(angle);
        if (!shooter.getDirection()) { // Si regarde à gauche
            rad = Math.PI - rad;
        }
        int offsetX = (int) (Math.cos(rad) * distance);
        int offsetY = (int) (-Math.sin(rad) * distance);
        Position startPos = new Position(
                shooter.getPosition().getX() + offsetX,
                shooter.getPosition().getY() + offsetY - 10 // Légèrement plus haut (épaules)
        );
        Projectile proj = createProjectile(shooter, startPos, angle, power);
        gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);

    }
    @Override
    public Projectile createProjectile(Alien shooter, Position startPosition, int angleDegrees,
                                       int powerPercent) {
        Rocket rocket = new Rocket(
                shooter,                          // Le tireur
                startPosition,                    // ✅ La position de départ (sortie du canon)
                this.getDamage(),                 // Les dégâts
                3,        // Rayon d'explosion
                true,                             // C'est explosif
                this.getSpeed(),                  // Vitesse
                this                              // L'arme (Bazooka)
        );
        return rocket;
    }

}
