package org.example.model.items;
import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Grenades_projectiles extends Arme implements Utilisable{
    private Position position;
    public Grenades_projectiles(String nom,int degat, int rayon) {
        super(nom,degat,rayon);
        this.position = new Position(0,0);
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

    //Méthode lancer qui renvoie la position du projectile/grenade après que un joueur les lance
    public void lancer(Worm wormActif) { }

    //Méthode explosion infligeant des dégats aux vers qui ont une position proche de la grenade
    public void explosion(Monde monde, Worm wormActif) {
        this.lancer(wormActif); // représente la position de la grenade après qu'un joueur l'est lanceé
        int position_grenade_x = this.getPosition().getX(); // position de la grenade en x
        int position_grenade_y = this.getPosition().getY(); // position de la grenade en y
        for ( Worm w : ) {
            int w_position_x =  w.getPosition().getX();
            int w_position_y =  w.getPosition().getY();
            boolean position_x = position_grenade_x <= w_position_x && w_position_x <= position_grenade_x + this.getRayonExplosion(); // vérifie que le vers est dans la zone de dégats de la grenade en x
            boolean position_y = position_grenade_y <= w_position_y && w_position_y <= position_grenade_y + this.getRayonExplosion(); // vérifie que le vers est dans la zone de dégats de la grenade en y
            if ( position_x && position_y ) {
                w.subirDegats(this.getDegats());
            }
        }
    }
}
