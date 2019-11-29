import java.util.Random;
import java.util.Scanner;

public class AI extends Player {

    private Random rn;

    /**
     * The default constructor of the class.
     */
    public AI() {
        super("Computer");
        rn = new Random();
    }

    /**
     * Place the AI's ships on its battlefield.
     *
     * @param game The current game session. It is used for displaying the updated battlefield.
     * @param in Could be null, it's not used in this method which override the parent (Player) class's method.
     */
    @Override
    public void placeShips(GameOfBattleships game, Scanner in) {
        int count = 0; // Used for counting the placed ships.
        for (int shipLength = 4; shipLength >= 1; shipLength--) {
            for (int pieces = 5 - shipLength; pieces >= 1; pieces--) {
                Position position; // Where to place the top-left end of the ship.
                int direction; // 0: horizontal, 1: vertical
                boolean valid = false; // Indicate whether the placement is valid or not.
                while (!valid) {
                    position = new Position(String.valueOf(
                                    Position.calculateColumnIndexFromInteger(rn.nextInt(Menu.NUMBER_OF_COLUMNS) + 1)) +
                                    (rn.nextInt(Menu.NUMBER_OF_ROWS) + 1)
                    );
                    direction = rn.nextInt(2);
                    // Check if the given place for the ship is allowed or not
                    if (isPlacementAllowed(position, direction, shipLength)) {
                        // Everything's ok, add the new ship.
                        Ship newShip = new Ship(shipLength, position, direction);
                        fleet[count] = newShip;
                        updateFieldsWithNewShip(newShip); // Update the AI's battlefield
                        count++;
                        valid = true; // Exit the loop
                        }
                }
            }
        }
    }
}
