package upc.model.entites.projectiles;
import upc.model.entites.Alien;
import upc.model.items.Weapon;
import upc.model.jeu.GameWorld;

public class Trap extends Projectile {

    private boolean active = true;
    private final int activationRadius;

    public Trap(Weapon weapon, Alien shooter, int activationRadius, int explosionRadius) {
        super(
                shooter,
                shooter.getPosition(),
                weapon.getDamage(),
                explosionRadius,
                false,
                weapon.getSpeed(),
                weapon);

        this.activationRadius = activationRadius;
    }


    //Méthode pour voir si un vers se situe dans la zone d'activation et provoque son explosion
    public void activation(GameWorld gameWorld) {
        if (!isActive()) { //si le piège a déjà explosé on ne peut plus la réutilisé
            return;
        }
        int posX = getPosition().getX();
        int posY = getPosition().getY();

        for (Alien w : gameWorld.aliensInRadius(posX, posY, activationRadius)) {
            if (w.isAlive() && w != getShooter()) {
                gameWorld.explode(posX, posY, getExplosionRadius(), getDamage());
                setActive(false);
            }
        }
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

