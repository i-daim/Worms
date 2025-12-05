package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileBalle extends Projectile {
    public ProjectileBalle(Weapon weapon, Alien shooter) {
        super(
                shooter,                       // Alien qui tire
                shooter.getPosition(),         // Position de départ
                weapon.getDamage(),            // Dégâts de la balle
                0,                             // Pas de rayon d'explosion
                false,                         // Pas explosif
                weapon.getPuissanceTir(),      // Vitesse de tir
                weapon                         // Référence à l'arme
        );
    }
}
