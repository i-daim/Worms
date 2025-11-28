package upc.model.entites;

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

    public Position deplacer(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    public double distanceAvec(Position autre) {
        int dx = this.x - autre.x;
        int dy = this.y - autre.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distance(int x, int y) {  // Retourner double
        int dx = this.x - x;
        int dy = this.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

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
