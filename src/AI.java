import java.util.Random;
import java.util.Scanner;

public class AI extends Player {

    private final Random rn;

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
     * @param in Could be null. It's not used in this method which overrides the parent (Player) class's method.
     */
    @Override
    public void placeShips(GameOfBattleships game, Scanner in) {
        for (int shipLength = 4; shipLength >= 1; shipLength--) {
            for (int pieces = 5 - shipLength; pieces >= 1; pieces--) {
                Position position; // Where to place the top-left end of the ship.
                int direction; // 0: horizontal, 1: vertical
                boolean valid = false; // Indicate whether the placement is valid or not.
                while (!valid) {
                    position = askCoordinate(in);
                    direction = rn.nextInt(2);
                    // Check if the given place for the ship is allowed or not
                    if (isPlacementAllowed(position, direction, shipLength)) {
                        // Everything's ok, add the new ship.
                        Ship newShip = new Ship(shipLength, position, direction);
                        fleet.add(newShip);
                        updateFieldsWithNewShip(newShip); // Update the AI's battlefield
                        valid = true; // Exit the loop
                    }
                }
            }
        }
    }

    @Override
    public Position askCoordinate(Scanner in) {
        return new Position(String.valueOf(
                Position.calculateColumnIndexFromInteger(rn.nextInt(Menu.NUMBER_OF_COLUMNS) + 1)) +
                (rn.nextInt(Menu.NUMBER_OF_ROWS) + 1)
        );
    }

    @Override
    public Character[] getBattlefieldData(int rowIndex) {
        Character[] data = new Character[Menu.NUMBER_OF_COLUMNS];

        Field[] row = battlefield[rowIndex];
        for (int i = 0, columnLength = row.length; i < columnLength; i++) {
            Field field = row[i];

            // Fill up the data array based on the fields' status
            char c;
            if (field.hasShip()) {
                if (field.isSank()) c = Menu.SANK_SHIP;
                else if (field.isFired()) c = Menu.HIT;
                else c = ' ';
            } else {
                if (field.isFired()) c = Menu.MISSED_SHOT;
                else c = ' ';
            }

            data[i] = c;
        }
        return data;
    }
}
