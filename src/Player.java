import java.util.Scanner;

/**
 * Represents a player of the Battleship game.
 * 
 * @author MiklosMayer
 *
 */
public class Player {

    private final String name;
	private int hits;
	private int misses;
	private Ship[] fleet;

	/**
	 * Rows first, columns second
	 */
	private Field[][] battlefield;

	public Player(String name) {
	    this.name = name;
		hits = 0;
		misses = 0;
		fleet = new Ship[10];
        battlefield = new Field[Menu.NUMBER_OF_COLUMNS][Menu.NUMBER_OF_ROWS];
		for (int i = 0, battlefieldLength = battlefield.length; i < battlefieldLength; i++) {
			Field[] column = battlefield[i];
			for (int j = 0, columnLength = column.length; j < columnLength; j++) {
				column[j] = new Field(new Position(
						String.valueOf(Position.calculateColumnIndexFromInteger(j + 1))
								+ (i + 1)));
			}
		}
	}
	
	public void placeShips(GameOfBattleships game, Scanner in) {
		int count = 0;
		String error = "";
		for (int shipLength = 4; shipLength >= 1; shipLength--) {
			for (int pieces = 5 - shipLength; pieces >= 1; pieces--) {
				Position position;
				int direction;
				boolean valid = false;
				while (!valid) {
					game.displayGrids();
					if (!error.isEmpty()) {
						System.out.println(error);
						System.out.println(Menu.LINE_SEPARATOR);
					}
					System.out.printf("Place your %s (%d long).\n", getShipName(shipLength), shipLength);
					System.out.println("Where would you like it's top-left end?");
					try {
						position = new Position(in.nextLine());
						if (shipLength > 1) direction = getDirection(in);
						else direction = 0;
						if (isPlacementAllowed(position, direction, shipLength)) {
							Ship newShip = new Ship(shipLength, position, direction);
							fleet[count] = newShip;
							updateFieldsWithNewShip(newShip);
							valid = true;
							count++;
							error = "";
						} else {
							error = "You cannot place this ship there. It collides with other ships or stick out from the battlefield.";
						}
					} catch (IllegalArgumentException e) {
						error = "Please enter a correct position (like 'B5').";
					}
				}
			}
		}
	}

	private boolean isPlacementAllowed(Position position, int direction, int length) {
		int x = position.getColumn() - 1;
		int y = position.getRow() - 1;
		if (direction == 0) {
			if (x + length > Menu.NUMBER_OF_COLUMNS) return false;
			for (int row = (y == 0 ? y : y - 1); row <= (y == Menu.NUMBER_OF_ROWS - 1 ? y : y + 1); row++) {
				for (int column = (x == 0 ? x : x - 1); column < (x + length == Menu.NUMBER_OF_COLUMNS ? x + length : x + length + 1); column++) {
					if (battlefield[row][column].hasShip()) return false;
				}
			}
		} else if (direction == 1) {
			if (y + length > Menu.NUMBER_OF_ROWS) return false;
			for (int row = (y == 0 ? y : y - 1); row < (y + length == Menu.NUMBER_OF_ROWS ? y + length : y + length + 1); row++) {
				for (int column = (x == 0 ? x : x - 1); column <= (x == Menu.NUMBER_OF_COLUMNS - 1 ? x : x + 1); column++) {
					if (battlefield[row][column].hasShip()) return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	private void updateFieldsWithNewShip(Ship ship) {
		int x = ship.getPosition().getColumn() - 1;
		int y = ship.getPosition().getRow() - 1;
		if (ship.getDirection() == 0) {
			for (int column = x; column < x + ship.getSize(); column++) {
				battlefield[y][column].placeShip(ship.getId());
			}
		} else if (ship.getDirection() == 1) {
			for (int row = y; row < y + ship.getSize(); row++) {
				battlefield[row][x].placeShip(ship.getId());
			}
		}
	}

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

	private int getDirection(Scanner in) {
		char direction;
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

    public String getName() {
        return name;
    }

    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }

    public Character[] getBattlefieldData(int rowIndex) {
		Character[] data = new Character[Menu.NUMBER_OF_COLUMNS];

		Field[] row = battlefield[rowIndex];
		for (int i = 0, columnLength = row.length; i < columnLength; i++) {
			Field field = row[i];

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
