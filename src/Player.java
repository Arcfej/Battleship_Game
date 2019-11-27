import java.util.Scanner;

/**
 * Represents a player of the Battleship game.
 * 
 * @author MiklosMayer
 *
 */
public class Player {

	/**
	 * The name of the player.
	 */
    private final String name;

	/**
	 * The number of fires which reached a ship.
	 */
	private int hits;

	/**
	 * The number of fires which reached water.
	 */
	private int misses;

	/**
	 * The remaining, not sank fleet of the player.
	 */
	private Ship[] fleet;

	/**
	 * The battlefield of the player with its ships placed (after placeShips() was called).
	 * The first dimension of the array is the rows, and the second dimension is the cells inside the rows.
	 */
	private Field[][] battlefield;

	/**
	 * The default constructor of the class.
	 *
	 * @param name The name of the player.
	 */
	public Player(String name) {
	    this.name = name;
		hits = 0;
		misses = 0;
		fleet = new Ship[10];
		// Fill up the battlefield with empty fields without ships.
        battlefield = new Field[Menu.NUMBER_OF_ROWS][Menu.NUMBER_OF_COLUMNS];
		for (int i = 0, battlefieldLength = battlefield.length; i < battlefieldLength; i++) {
			Field[] row = battlefield[i];
			for (int j = 0, columnLength = row.length; j < columnLength; j++) {
				row[j] = new Field(new Position(
						String.valueOf(Position.calculateColumnIndexFromInteger(j + 1))
								+ (i + 1)));
			}
		}
	}

	/**
	 * Ask the user where to place their ships and update the displaying in the process.
	 *
	 * @param game The current game session. It is used for displaying the updated battlefield.
	 * @param in The input stream through the user communicates with the program.
	 */
	public void placeShips(GameOfBattleships game, Scanner in) {
		// TODO save the state after every successful placement
		int count = 0; // Used for counting the placed ships.
		String error = ""; // Used for displaying error messages.

		// Get the input for the 1 x 4 long, 2 x 3 long, 3 x 2 long and 4 x 1 long ships
		for (int shipLength = 4; shipLength >= 1; shipLength--) {
			for (int pieces = 5 - shipLength; pieces >= 1; pieces--) {
				Position position; // Where to place the top-left end of the ship.
				int direction; // 0: horizontal, 1: vertical
				boolean valid = false; // Indicate whether the input is valid or not.
				while (!valid) {
					// Display the battlefields before every input from the user.
					game.displayGrids();
					// If there was an error in the previous iteration of the loop, display it.
					if (!error.isEmpty()) {
						System.out.println(error);
						System.out.println(Menu.LINE_SEPARATOR);
					}
					System.out.printf("Place your %s (%d long).\n", getShipName(shipLength), shipLength);
					System.out.println("Where would you like it's top-left end?");
					try {
						position = new Position(in.nextLine());
						// Only ask for the direction if there wasn't an error while asking for the position (which is caught),
						// and the ship is longer than 1.
						if (shipLength > 1) direction = getDirection(in);
						else direction = 0;
						// Check if the given place for the ship is allowed or not
						if (isPlacementAllowed(position, direction, shipLength)) {
							// Everything's ok, add the new ship.
							Ship newShip = new Ship(shipLength, position, direction);
							fleet[count] = newShip;
							updateFieldsWithNewShip(newShip); // Update the player's battlefield
							count++;
							error = ""; // There was no error through the current iteration
							valid = true; // Exit the loop
						} else {
							// If the ship's not allowed at the given position, store the error message for the next iteration.
							error = "You cannot place this ship there. It collides with other ships or stick out from the battlefield.";
						}
					} catch (IllegalArgumentException e) {
						// Error during asking for the position of the ship's top-left
						error = "Please enter a correct position (like 'B5').";
					}
				}
			}
		}
	}

	/**
	 * Checks if the ship is allowed at the given position with the given direction.
	 *
	 * @param position The position of the top-left end of the ship.
	 * @param direction The direction of the ship. 0: horizontal, 1: vertical.
	 * @param length The length of the ship.
	 * @return true if the ship is allowed to place at the given position.
	 */
	private boolean isPlacementAllowed(Position position, int direction, int length) {
		// The coordinates of the position inside the grid
		int x = position.getColumn() - 1;
		int y = position.getRow() - 1;

		// Check if there is any problem with the placement and return false if so

		// In case the ship is horizontal
		if (direction == 0) {
			// If the ship's stick out from the grid, return false
			if (x + length > Menu.NUMBER_OF_COLUMNS) return false;
			// Start from 1 before the ship's position horizontally and end 1 after it, but remain inside the grid.
			for (int row = (y == 0 ? y : y - 1); row <= (y == Menu.NUMBER_OF_ROWS - 1 ? y : y + 1); row++) {
				// Start from 1 before the ship's position vertically and end 1 after it, but remain inside the grid.
				for (int column = (x == 0 ? x : x - 1); column < (x + length == Menu.NUMBER_OF_COLUMNS ? x + length : x + length + 1); column++) {
					if (battlefield[row][column].hasShip()) return false;
				}
			}
		}
		// In case the ship is vertical
		else if (direction == 1) {
			// If the ship's stick out from the grid, return false
			if (y + length > Menu.NUMBER_OF_ROWS) return false;
			// Start from before the ship's position vertically, but remain inside the grid.
			for (int row = (y == 0 ? y : y - 1); row < (y + length == Menu.NUMBER_OF_ROWS ? y + length : y + length + 1); row++) {
				// Start from 1 before the ship's position horizontally and end 1 after it, but remain inside the grid.
				for (int column = (x == 0 ? x : x - 1); column <= (x == Menu.NUMBER_OF_COLUMNS - 1 ? x : x + 1); column++) {
					if (battlefield[row][column].hasShip()) return false;
				}
			}
		}
		// In case the given direction is invalid
		else {
			return false;
		}
		// If there wasn't any problem, return true
		return true;
	}

	/**
	 * Updates the battlefield with the new ship's information
	 *
	 * @param ship The newly placed ship
	 */
	private void updateFieldsWithNewShip(Ship ship) {
		// The coordinates of the top-left end of the ship
		int x = ship.getPosition().getColumn() - 1;
		int y = ship.getPosition().getRow() - 1;

		// Diverge based on the ship's direction
		if (ship.getDirection() == 0) {
			// Update the field at the ship's position and the ones right from it according to its length
			for (int column = x; column < x + ship.getSize(); column++) {
				battlefield[y][column].placeShip(ship.getId());
			}
		} else if (ship.getDirection() == 1) {
			// Update the field at the ship's position and the ones below it according to its length
			for (int row = y; row < y + ship.getSize(); row++) {
				battlefield[row][x].placeShip(ship.getId());
			}
		}
	}

	/**
	 * Returns the ship's name based on its length.
	 *
	 * @param shipLength The length of the ship.
	 * @return The name of the ship
	 */
	private String getShipName(int shipLength) {
		switch (shipLength) {
			case 4:
				return "Battleship";
			case 3:
				return "Cruiser";
			case 2:
				return "Destroyer";
			case 1:
				return "Submarine";
			default:
				return "";
		}
	}

	/**
	 * Gets the direction of a new ship from the user.
	 *
	 * @param in The input stream through the user communicates with the program.
	 * @return 0 if the user want horizontal direction and 1 in case of a vertical one.
	 */
	private int getDirection(Scanner in) {
		char direction;
		// Continue asking the the user until they provide a valid input.
		while (true) {
			System.out.println("Would you like it to be horizontal ('h') or vertical ('v')?");
			String input = in.nextLine();
			if (input.length() == 1) {
				direction = input.charAt(0);
				if (direction == 'h') {
					return 0;
				} else if (direction == 'v') {
					return 1;
				}
			}
			System.out.println("Not a valid input.");
		}
	}

	public int takeFire(Position target) {
		System.out.println("TODO: implement takeFire");
		Scanner in = new Scanner(System.in);
		System.out.println("0 - miss; 1 - hit; 2 - sink; -1 exit");
		if (in.hasNextInt()) {
			return in.nextInt();
		} else {
			in.next();
		}
		return -1;
	}

	/**
	 * Return the name of the player.
	 *
	 * @return the name of the player
	 */
    public String getName() {
        return name;
    }

	/**
	 * Return the number of fires committed by the player which hit a ship.
	 *
	 * @return the number of hits by the player.
	 */
	public int getHits() {
        return hits;
    }

	/**
	 * Return the number of fires committed by the player which did not hit a ship.
	 *
	 * @return the number of misses by the player.
	 */
	public int getMisses() {
        return misses;
    }

	/**
	 * Return a row of displayable data about the player's battlefield.
	 *
	 * @param rowIndex The index of the row to get data from.
	 * @return The array of displayable data as characters.
	 */
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
				else c = Menu.SHIP;
			} else {
				if (field.isFired()) c = Menu.MISSED_SHOT;
				else c = ' ';
			}

			data[i] = c;
		}
		return data;
    }
}
