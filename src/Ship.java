public class Ship {

    private static int count;

    private final int id;
    private final int size;
    private final Position position;

    /**
     * 0 if horizontal, 1 if vertical.
     */
    private final int direction;
    private int hits;
    private int sankPercent;

    public Ship(int size, Position position, int direction) {
        count++;
        id = count;
        this.size = size;
        this.position = position;
        this.direction = direction;
        sankPercent = 0;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public Position getPosition() {
        return position;
    }

    public int getDirection() {
        return direction;
    }

    public int getSankPercent() {
        return sankPercent;
    }

    public void takeHit() {
        sankPercent = (int) ((double) ++hits / size * 100);
    }
}
