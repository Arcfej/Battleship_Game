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
     * The first player;
     */
	private Player player1;

    /**
     * The second player
     */
	private Player player2;
	
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
		player1 = new Player();
		player2 = new Player();
		activePlayer = player1;
		passivePlayer = player2;
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
			displayGrids();
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
		System.out.println("What is your target? (e.g. 'A1')");
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
	    int widthOfLabel = 5;
	    int spaceTillLabel = (int) Math.ceil((Menu.TABLE_WIDTH - widthOfLabel) / 2f);
	    int firstPosition = spaceTillLabel + widthOfLabel;
	    int secondPosition = (Menu.TABLE_WIDTH - firstPosition + Menu.GAP + spaceTillLabel + widthOfLabel);
	    System.out.println(String.format("%" + firstPosition + "s%"+ secondPosition + "s", "Enemy", "Yours"));
        for (int row = 0; row < Menu.NUMBER_OF_ROWS; row++) {
//			StringBuilder formatter = new StringBuilder();
//			for (int i = 1; i <= Menu.TABLE_WIDTH; i++) {
//				formatter.append("$").append(i).append("%s|");
//			}
//			formatter.deleteCharAt(formatter.length() - 1);
//			formatter.append(" ".repeat(Menu.GAP));
//			for (int i = 1; i <= Menu.TABLE_WIDTH; i++) {
//				formatter.append("$").append(i).append("%s|");
//			}
//			formatter.deleteCharAt(formatter.length() - 1);
//			Character[] xs = new Character[Menu.NUMBER_OF_COLUMNS];
//			for (Character c : xs) {
//				c = 'X';
//			}
//			System.out.println(String.format(formatter.toString(), xs));

			System.out.print(" " + Menu.COLUMN_SEPARATOR);
			for (int i = 0; i < Menu.NUMBER_OF_COLUMNS; i++) {
				System.out.print((char) (i + 65));
				if (i < Menu.NUMBER_OF_COLUMNS - 1) System.out.print("|");
			}

			System.out.print(" ".repeat(Menu.GAP));

			System.out.print(" " + Menu.COLUMN_SEPARATOR);
			for (int i = 0; i < Menu.NUMBER_OF_COLUMNS; i++) {
				System.out.print((char) (i + 65));
				if (i < Menu.NUMBER_OF_COLUMNS - 1) System.out.print("|");
			}
			System.out.println();

			Character[] xs = new Character[Menu.NUMBER_OF_COLUMNS];
			for (Character c : xs) {
				c = 'X';
			}

			System.out.println(Menu.SEPARATOR);
        }
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
