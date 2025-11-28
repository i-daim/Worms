package upc.model.items;


public class StickyBomb extends Throwable {
    public static final int DEGATS = 25;
    public static final int RAYON = 5;

    public StickyBomb(String nom) {
        super(nom, DEGATS, RAYON);
    }
}
