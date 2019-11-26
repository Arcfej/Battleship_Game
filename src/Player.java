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
	
	public void placeShips() {
		System.out.println("TODO: place ships");
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
