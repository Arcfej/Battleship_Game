/**
 * Represents a ship in the Battleship game.
 *
 * @author MiklosMayer
 */
public class Ship {

    /**
     * Used to generate the ids for the ships.
     */
    private static int count = 0;

    /**
     * The unique id of the ship.
     */
    private final int id;

    /**
     * The size of the ship.
     */
    private final int size;

    /**
     * The position of the top-left end of the ship.
     */
    private final Position position;

    /**
     * The direction of the ship. 0 if horizontal, 1 if vertical.
     */
    private final int direction;

    /**
     * The number of hits the ship has received.
     */
    private int hits;

    /**
     * The percentage of damage the ship has received. 0 if it hasn't been hit and 100 if it sank.
     */
    private int sankPercent;

    /**
     * The default constructor of the class.
     *
     * @param size The size of the ship.
     * @param position The top-left end of the ship.
     * @param direction The direction of the ship. 0 if horizontal, 1 if vertical.
     */
    public Ship(int size, Position position, int direction) {
        count++;
        id = count;
        this.size = size;
        this.position = position;
        this.direction = direction;
        sankPercent = 0;
    }

    /**
     * Returns the unique id of the ship.
     *
     * @return the id of the ship.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the size of the ship.
     *
     * @return the size of the ship.
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the position of the top-left end of the ship.
     *
     * @return the top-left end of the ship.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the direction of the ship.
     *
     * @return 0 if the ship is horizontal, 1 if vertical.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Returns the percentage of damage the ship has received.
     *
     * @return a whole number between 0 and 100. 0 if it hasn't been hit and 100 if it sank.
     */
    public int getSankPercent() {
        return sankPercent;
    }

    /**
     * Registers a hit on the ship.
     */
    public void takeHit() {
        sankPercent = (int) ((double) ++hits / size * 100);
    }
}
