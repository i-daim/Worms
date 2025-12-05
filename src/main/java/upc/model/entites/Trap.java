package upc.model.entites;

import upc.model.jeu.GameWorld;

public class Trap extends Entity {

    private int damage;
    private int activationRadius;
    private int explosionRadius;
    private boolean active = true;


    public Trap(Position position, int damage, int activationRadius, int explosionRadius) {
        super(position, 0);
        this.damage = damage;
        this.activationRadius = activationRadius;
        this.explosionRadius = explosionRadius;
    }

    //Méthode activation voir si un vers se situe à la position d'un piège et provoque son explosion
    public boolean activation(GameWorld gameWorld) {
        if (!isActive()) { //si le piège a déjà explosé on ne peut plus la réutilisé
            return false;
        }
        int posX = getPosition().getX();
        int posY = getPosition().getY();

        for (Alien w : gameWorld.aliensInRadius(posX, posY, activationRadius)) {
            if (w.getPosition().getX() == posX && w.getPosition().getY() == posY) {
                return true;
            }
        }
        return false;
    }

    //Méthode explosion infligeant des dégats aux vers qui ont une position proche d'un piège
    private void explosion(GameWorld gameWorld) {
        int posPiegeX = getPosition().getX();
        int posPiegeY = getPosition().getY();

        gameWorld.explode(posPiegeX, posPiegeY, explosionRadius, damage);
        setActive(false);
        gameWorld.removeTrap(this);
    }


    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}
