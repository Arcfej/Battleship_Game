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
	 * Columns first, rows second
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
						String.valueOf(Position.calculateColumnIndexFromInteger(i + 1))
								+ (j + 1)));
			}
		}
	}
	
	public void placeShips(GameOfBattleships game, Scanner in) {
		int count = 0;
		for (int shipLength = 4; shipLength >= 1; shipLength--) {
			for (int pieces = 5 - shipLength; pieces >= 1; pieces--) {
				Position position;
				int direction;
				boolean valid = false;
				while (!valid) {
					game.displayGrids();
					System.out.printf("Place your %s (%d long).\n", getShipName(shipLength), shipLength);
					System.out.println("Where would you like it's top-left end?");
					try {
						position = new Position(in.nextLine());
						direction = getDirection(in);
						System.out.println("TODO: Validate ship placement");
						System.out.println("TODO: Update fields with ship id");
						fleet[count] = new Ship(4, position, direction);
						valid = true;
						count++;
					} catch (IllegalArgumentException e) {
						System.out.println("Please enter a correct position (like 'B5').");
					}
				}
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

    public Character[][] getBattlefieldData() {
		Character[][] data = new Character[Menu.NUMBER_OF_COLUMNS][Menu.NUMBER_OF_ROWS];

		for (int i = 0, battlefieldLength = battlefield.length; i < battlefieldLength; i++) {
			Field[] column = battlefield[i];

			for (int j = 0, columnLength = column.length; j < columnLength; j++) {
				Field field = column[j];
				char c;

				if (field.hasShip()) {
					if (field.isSank()) c = Menu.SANK_SHIP;
					else if (field.isFired()) c = Menu.HIT;
					else c = Menu.SHIP;
				} else {
					if (field.isFired()) c = Menu.MISSED_SHOT;
					else c = ' ';
				}

				data[i][j] = c;
			}
		}
		return data;
    }
}
