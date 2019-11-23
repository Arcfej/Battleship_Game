import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Represents a Battleship game. Handles all the interactions between the players.
 * 
 * @author MiklosMayer
 *
 */
public class GameOfBattleships {

    /**
     * The number of rows of the battleship gamefield.
     */
    public static final int NUMBER_OF_ROWS = 10;

    /**
     * The number of Columns of the battleship gamefield.
     */
    public static final int NUMBER_OF_COLUMNS = 10;

    /**
	 * The menu of the game which handles e.g. the saving of the game state.
	 */
	private final Menu menu;
	
	/**
	 * The input stream through the user communicates with the program.
	 */
	private Scanner in;
	
	/**
	 * The number of rounds the players played.
	 */
	private int rounds;
	
	/**
	 * The player who is firing on the other
	 */
	private Player activePlayer;
	
	/**
	 * The player who is taking the fire. 
	 */
	private Player passivePlayer;

	/**
	 * Default constructor of the class.
	 * 
	 * @param menu The menu of the game which handles e.g. the saving of the game state.
	 * @param in The input stream through the user communicates with the program.
	 */
	public GameOfBattleships(Menu menu, Scanner in) {
		this.menu = menu;
		this.in = in;
		rounds = 1;
		activePlayer = new Player();
		passivePlayer = new Player();
	}
	
	/**
	 * The whole cycle of an entire game, from placing the ships to winning the game.
	 */
	public void play() {
		// Placing the ships.
		activePlayer.placeShips();
		passivePlayer.placeShips();
		
		// Loop: Firing on each other.
		boolean end = false;
		while (!end) {
			menu.saveGame();
			System.out.println("TODO: what if save unsuccessful?");
			System.out.println("TODO: AI choose its target differently");
			System.out.println("The game is saved. You can exit to the Main Menu by typing in 'Exit'");
			Position target;
			// Loop: get a valid input from the user
			while (true) {
				try {
					target = askCoordinate();
				} catch (InputMismatchException e) {
					// Exit the game if the user would like to.
					if (e.getMessage().equals("Exit")) return;
					// Continue asking a valid input from the user
					else {
					    System.out.println("\nNot a valid target. (For exit, type in 'Exit')");
                        continue;
                    }
				}
				System.out.println("TODO: check if the target is previously fired upon");
				break;
			}
			// Fire on the target provided by the player
			switch(passivePlayer.takeFire(target)) {
				// Missed
				case 0:
					System.out.println("TODO: increase misses");
					rounds++;
					switchPlayers();
					break;
				// Hit
				case 1:
					System.out.println("TODO: increase hits");
					break;
				// Sank
				case 2:
					System.out.println("TODO: implement sank possibilities");
					System.out.println("1 - all the ships sank");
					if (in.hasNextInt() && in.nextInt() == 1) {
						end = true;
					}
					break;
				// Exit
				case -1:
					return;
				default:
					System.out.println("Not a valid input!");
			}
		}
		
		// Wrap up the game in the end (saving score, delete saved game).
		System.out.println("TODO: calculate score for winner");
		menu.deleteSavedGame();
		System.out.println("TODO: what if delete unsuccessful?");
	}
	
	/**
	 * Ask for coordinates from the active player to fire upon.
	 * 
	 * @return the position of the target.
	 * @throws InputMismatchException when the input provided by the user is not a coordinate.
	 * 								  The exception contains the input in its message.
	 */
	private Position askCoordinate() throws InputMismatchException {
		System.out.println("What is your target? (e.g. 'A1'");
		System.out.println(Menu.SEPARATOR);
		String input = in.nextLine();
		try {
			return new Position(input);
		} catch (IllegalArgumentException e) {
			throw new InputMismatchException(input);
		}
	}

	/**
	 * Display the battlefields to the user(s).
	 */
	private void displayGrids() {
		System.out.println("TODO: display the battlefield");
	}
	
	/**
	 * @return the number of rounds the players played so far.
	 */
	public int getRounds() {
		return rounds;
	}
	
	/**
	 * Switch the active and passive player of the game.
	 */
	private void switchPlayers() {
		Player holder = activePlayer;
		activePlayer = passivePlayer;
		passivePlayer = holder;
	}
}
