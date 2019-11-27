public class Field {

    private final Position position;
    private boolean hasShip;
    private boolean isFired;
    private boolean isSank;
    private int shipId;

    public Field(Position position) {
        this.position = position;
        hasShip = false;
        isFired = false;
        isSank = false;
        shipId = -1;
    }

    public void placeShip(int shipId) {
        hasShip = true;
        this.shipId = shipId;
    }

    public Position getPosition() {
        return position;
    }

    public boolean hasShip() {
        return hasShip;
    }

    public boolean isFired() {
        return isFired;
    }

    public boolean isSank() {
        return isSank;
    }

    public int getShipId() {
        return shipId;
    }
}
