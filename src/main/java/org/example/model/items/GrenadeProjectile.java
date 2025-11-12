package org.example.model.items;
import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public abstract class GrenadeProjectile extends Arme implements Utilisable {
    private Position position;
    public GrenadeProjectile(String nom, int degat, int rayon) {
        super(nom, degat, rayon);
        this.position = new Position(0, 0);
    }

    @Override
    public void utiliser(Monde monde, Worm wormActif) {
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
    public void lancer(Worm wormActif, int puissance, int angle) {
        int posVersX = wormActif.getPosition().getX();
        int posVersY = wormActif.getPosition().getY();
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
    public void explosion(Monde monde, Worm wormActif) {
        int posPiegeX = this.getPosition().getX();
        int posPiegeY = this.getPosition().getY();
        for (Worm w : monde.wormsDansRayon(posPiegeX, posPiegeY, this.getRayonExplosion())) {
            if (w == null) {
                return;
            }
            wormActif.subirDegats(this.getDegats());
        }
    }
}
