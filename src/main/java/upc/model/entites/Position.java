package upc.model.entites;
/**
 * Représente une position dans un plan en deux dimensions.
 * Une position est définie par des coordonnées entières (x, y).
 * La classe est immuable : ses valeurs ne peuvent pas être modifiées.
 */
public class Position {
    private final int x;
    private final int y;
    private static final int HASHCODE = 31;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Retourne une nouvelle position déplacée par rapport à la position actuelle.
     *  La position courante n'est pas modifiée.
     * @param dx déplacement sur l'axe x
     * @param dy déplacement sur l'axe y
     * @return
     */
    public Position deplacer(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }
/**
 * Calcule la distance entre cette position et une autre position.
 *
 * @param autre la position avec laquelle on calcule la distance
 * @return la distance entre les deux positions
 */
    public double distanceAvec(Position autre) {
        int dx = this.x - autre.x;
        int dy = this.y - autre.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    /**
     * Calcule la distance entre cette position et un point donné.
     *
     * @param x coordonnée x du point
     * @param y coordonnée y du point
     * @return la distance entre la position et le point donné
     */
    public double distance(int x, int y) {  // Retourner double
        int dx = this.x - x;
        int dy = this.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    /**
     * Indique si l'objet donné est égal à cette position.
     * Deux positions sont égales si leurs coordonnées x et y sont identiques.
     *
     * @param o l'objet à comparer
     * @return true si les positions sont égales, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position position)) {
            return false;
        }
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return HASHCODE * x + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
