package org.example.model.items;

import org.example.model.entites.Alien;
import org.example.model.jeu.GameWorld;
import org.example.model.entites.Position;

public abstract class Throwable extends Weapon implements Usable {
    private Position position;
    public Throwable(String nom, int degat, int rayon) {
        super(nom, degat, rayon);
        this.position = new Position(0, 0);
    }

    @Override
    public void utiliser(GameWorld gameWorld, Alien alienActif) {
    }
    @Override
    public double getInitialVelocity() {
        return 18.0;
    }
    @Override
    public boolean isAffectedByWind() {
        return true; // Les balles sont peu affectées par le vent
    }

    //getter
    public Position getPosition() {
        return position;
    }

    //setter
    public void setPosition(Position position) {
        this.position = position;
    }

    //Méthode lancer qui renvoie la position du projectile/grenade après qu'un joueur les lancé
    public void lancer(Alien alienActif, int puissance, int angle) {
        int posVersX = alienActif.getPosition().getX();
        int posVersY = alienActif.getPosition().getY();
        int limite = 20;
        if (puissance > limite) {
            System.out.println("La puissance dépase la limite autorisée. Recommencez"); //
            return;
        }
        if (angle < 0 || angle > 180) {
            System.out.println("L'angle doit être entre 0 et 180°. Recommencez");
            return;
        }
        double positionX = puissance * Math.cos(angle); //Renvoie la position en x
        double positionY = puissance * Math.sin(angle); //Renvoie la position en y
        //conversion en int pour correspondre à Position
        int posProjectileX = (int) (positionX + posVersX);
        int posProjectileY = (int) (positionY + posVersY);
        //renvoie nouvelle position du projectile après son lancement
        this.setPosition(new Position(posProjectileX, posProjectileY));

    }

    //Méthode explosion infligeant des dégats aux vers qui ont une position proche de la grenade
    public void explosion(GameWorld gameWorld, Alien alienActif) {
        int posPiegeX = this.getPosition().getX();
        int posPiegeY = this.getPosition().getY();
        for (Alien w : gameWorld.aliensInRadius(posPiegeX, posPiegeY, this.getRayonExplosion())) {
            if (w == null) {
                return;
            }
        }
    }

}
