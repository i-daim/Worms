package upc.model.items;

import upc.model.entites.Alien;
import upc.model.entites.projectiles.UziBullet;
import upc.model.jeu.GameWorld;
import upc.model.entites.projectiles.Projectile;

public class Uzi extends Weapon {

    public Uzi() {
        super("Uzi", 10, 30, 80);
    }

    @Override
    public void useItem(GameWorld gameWorld, Alien shooter) {
        int angle = shooter.getAimAngle();
        int power = shooter.getShootPower();

        Projectile proj = new UziBullet(this, shooter, shooter.getPosition());
        gameWorld.getPhysicsEngine().addProjectile(proj, angle, power);
    }

}
