package upc.model.items;

import upc.model.entites.Alien;
import upc.model.entites.Projectile;
import upc.model.jeu.GameWorld;

public abstract class Weapon implements Usable {
    private String nom;
    private int degats;
    private int rayonExplosion;
    private int nbTir;

    public Weapon(String nom, int degats, int rayonExplosion, int nbTir) {
        this.nom = nom;
        this.degats = degats;
        this.rayonExplosion = rayonExplosion;
        this.nbTir = nbTir;
    }

    public String getNom() {
        return nom;
    }
    public int getDegats() {
        return degats;
    }
    public int getRayonExplosion() {
        return rayonExplosion;
    }
    public int getNbTir() {
        return nbTir;
    }

    @Override
    public void utiliser(GameWorld gameWorld, Alien alienActif) {

    }

    public double getInitialVelocity() {
        return 0;
    }

    public boolean isAffectedByWind() {
        return false;
    }

    public Projectile createProjectile(Alien shooter) {
        int x = shooter.getPosition().getX();
        int y = shooter.getPosition().getY();
        return new Projectile(this, shooter, x, y);
    }

    //Méthode permettant de tirer et infliger des dégâts
    public void tir(GameWorld gameWorld, Alien alienActif) {
        int wormActifPositionX = alienActif.getPosition().getX();
        int wormActifPositionY = alienActif.getPosition().getY();
        int portee = this.getRayonExplosion();
        if (this.getNbTir() == 0) {
            System.out.println("Le worm n'a plus de munitions !");
            return;
        }
    }


}
