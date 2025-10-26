package variants.quoridor;

public final class Wall {
    public final int x, y;
    public final Orientation o;

    public Wall(int x, int y, Orientation o) {
        this.x = x;
        this.y = y;
        this.o = o;
    }
}
