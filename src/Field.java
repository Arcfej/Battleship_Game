import java.io.Serializable;

/**
 * Represents a field on the Battleship game, or cell in the grid of the battlefield.
 *
 * @author MiklosMayer
 */
public class Field implements Serializable {

    /**
     * The position of the field on the battlefield.
     */
    private final Position position;

    /**
     * True if the field has a ship, or part of a ship on it.
     */
    private boolean hasShip;

    /**
     * True if the field is previously fired upon.
     */
    private boolean isFired;

    /**
     * True if the field has a sunk ship, or part of a sunk ship on it.
     */
    private boolean isSank;

    /**
     * The unique id of a ship on the field, or -1 if it's empty.
     */
    private int shipId;

    /**
     * The default constructor of the class. Create an empty field without any ships on it.
     *
     * @param position The position of the field on the battlefield.
     */
    public Field(Position position) {
        this.position = position;
        hasShip = false;
        isFired = false;
        isSank = false;
        shipId = -1;
    }

    /**
     * Places a ship, or part of a ship on the field.
     *
     * @param shipId The unique id of the placed ship.
     */
    public void placeShip(int shipId) {
        hasShip = true;
        this.shipId = shipId;
    }

    /**
     * Registers that the other player fired on this field.
     *
     * @param isSank true if the ship on this field has sunk.
     */
    public void takeFire(boolean isSank) {
        isFired = true;
        this.isSank = isSank;
    }

    /**
     * Return the position of the field on the battlefield.
     *
     * @return the position of the field on the battlefield.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Return if the field has a ship or part of a ship on it or not.
     *
     * @return true if the field has a ship or part of a ship on it.
     */
    public boolean hasShip() {
        return hasShip;
    }

    /**
     * Return if the field has been previously fired upon it or not.
     *
     * @return true if the field has been previously fired upon it.
     */
    public boolean isFired() {
        return isFired;
    }

    /**
     * Return if the field has a sunk ship on it or not.
     *
     * @return true if the field has a sunk ship on it.
     */
    public boolean isSank() {
        return isSank;
    }

    /**
     * Return the unique id of the ship on the field.
     *
     * @return the unique id of the ship on it, or -1 if there isn't any ships there.
     */
    public int getShipId() {
        return shipId;
    }
}
