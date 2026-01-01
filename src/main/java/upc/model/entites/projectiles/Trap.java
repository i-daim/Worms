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
                true,
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



            for (Alien alien : gameWorld.getActiveAliens()) {
                if (alien != null && alien.isAlive() && alien != getShooter()) {
                    double distance = alien.getPosition().distance(posX, posY);

                    if (distance <= activationRadius) {
                        setState(State.EXPLODED);
                        setActive(false);

                        // dégâts progressifs
                        applyExplosionDamage(gameWorld);

                        // terrain + animation
                        gameWorld.getTerrain().makeHole(posX, posY, getExplosionRadius());
                        gameWorld.getPhysicsEngine()
                                .getTerrainPhysics()
                                .setModified(true);

                        gameWorld.getGameManager()
                                .addActiveAnimation("explosion", posX, posY);

                        return; // explosion une seule fois
                    }
                }
            }
        }





    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public int getActivationRadius() {
        return activationRadius;
    }

}

