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
	private Field[][] battlefield;

	public Player(String name) {
	    this.name = name;
		hits = 0;
		misses = 0;
		fleet = new Ship[10];
        battlefield = new Field[Menu.NUMBER_OF_COLUMNS][Menu.NUMBER_OF_ROWS];
	}
	
	public void placeShips(Scanner in) {
		int count = 0;
		for (int shipType = 4; shipType >= 1; shipType--) {
			for (int pieces = 5 - shipType; pieces >= 1; pieces--) {
				Position position;
				int direction;
				boolean valid = false;
				while (!valid) {
					System.out.printf("Place your Battleship (%d long).\n", shipType);
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

    public Field[][] getBattlefield() {
        return battlefield;
    }
}
