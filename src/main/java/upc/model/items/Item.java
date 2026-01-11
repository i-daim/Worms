package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameWorld;

public interface Item {

    /**
     * Représente l'ensemble des items du jeu
     */
    enum Type {
        SHOTGUN,
        UZI,
        BAZOOKA,
        GRENADE,
        MINE,
        WINGS
    }

    /**
     * Méthode permettant l'utilisation de l'arme shotgun
     * @param gameWorld : Le monde du jeu
     * @param alienActif : le tireur
     * @return l'objet {@link Projectile} représentant le projectile de l'arme
     */
    Projectile useItem(GameWorld gameWorld, Alien alienActif);

    /**
     * Renvoie le nombre max d'utilisations par tour pour l'item
     * @return l'attribut statique NB_OF_SHOTS pour chaque item
     */
    int getNbOfShots();

    /**
     * Retourne le nom de l'outil
     * @return le string {@link String} représentant le nom de l'outil
     */
    String getName();

    /**
     * Méthodant de retourner le type d'item correspondant à l'arme
     * @return l'objet {@link Type} représentant l'item en lien avec l'arme
     */
    Type getType();
}
