package upc.model.jeu;

public class Converter {
    public static final double SCALE = 20.0;

    public static double toMeters(int pixels) {
        return pixels / SCALE;
    }

    public static int toPixels(double meters) {
        return (int) (meters * SCALE);
    }
}
