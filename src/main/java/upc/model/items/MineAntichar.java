package upc.model.items;

public class MineAntichar extends Trap {
    public static final int DEGATS = 60;
    public static final int RAYON = 3;
    public MineAntichar(String nom) {
        super(nom, DEGATS, RAYON);
    }
}
