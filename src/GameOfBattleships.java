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
		player1 = new Player("TODO: Add name");
		player2 = new Player("TODO: Add name");
		activePlayer = player1;
		passivePlayer = player2;
	}
	
	/**
	 * The whole cycle of an entire game, from placing the ships to winning the game.
	 */
	public void play() {
		// Placing the ships.
		activePlayer.placeShips(in);
		passivePlayer.placeShips(in);
		
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
		System.out.println(Menu.LINE_SEPARATOR);
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
		// Numbers which help positioning the labels above the tables to the middle.
	    int spaceTillFirstLabel = (int) Math.ceil((Menu.TABLE_WIDTH - passivePlayer.getName().length()) / 2f);
	    int firstPosition = spaceTillFirstLabel + passivePlayer.getName().length();
	    int spaceTillSecondLabel = (int) Math.ceil((Menu.TABLE_WIDTH - activePlayer.getName().length()) / 2f);
	    int secondPosition = (Menu.TABLE_WIDTH - firstPosition + Menu.GAP + spaceTillSecondLabel + activePlayer.getName().length());
	    // Display the labels above the tables
	    System.out.println();
	    System.out.printf(
	    		"%" + firstPosition + "s%"+ secondPosition + "s\n",
				passivePlayer.getName().toUpperCase(),
				activePlayer.getName().toUpperCase()
		);
	    System.out.println();
	    
	    // First line with the column indexes
	    Character[] columnIndexes = new Character[Menu.NUMBER_OF_COLUMNS];
	    for (int i = 0; i < Menu.NUMBER_OF_COLUMNS; i++) {
	    	// Calculate the ascii codes of the indexes
	    	columnIndexes[i] = (char) (i + 65);
	    }
	    System.out.println(
	    		generateTableRow("", columnIndexes)
				+ " ".repeat(Menu.GAP)
				+ generateTableRow("", columnIndexes)
		);
	    // Display a border between two table rows
		System.out.println(generateTableRowSeparator() + " ".repeat(Menu.GAP) + generateTableRowSeparator());
	    
	    // Battlefields line by line
        for (int row = 0; row < Menu.NUMBER_OF_ROWS; row++) {
        	System.out.println(
        			generateTableRow(String.valueOf(row + 1), columnIndexes)
					+ " ".repeat(Menu.GAP)
					+ generateTableRow(String.valueOf(row + 1), columnIndexes)
			);
        	// Display a border between two table rows
			System.out.println(generateTableRowSeparator() + " ".repeat(Menu.GAP) + generateTableRowSeparator());
        }
        System.out.println();
	}

	/**
	 * Generate a separator line between two rows of a battleship table
	 *
	 * @return the generated line as a String
	 */
	private String generateTableRowSeparator() {
		String lineSeparator = "—————";
		String[] cells = new String[Menu.NUMBER_OF_COLUMNS];
	    for (int i = 0; i < Menu.NUMBER_OF_COLUMNS; i++) {
	    	cells[i] = lineSeparator;
	    }
	    return generateTableRow(lineSeparator, cells);
	}

	/**
	 * Generate a row for a table of battleship.
	 *
	 * @param rowIndex The index of the row to display at the start of the row.
	 * @param rowData The array of data to display in the cells.
	 * @param <T> The type of the data to display.
	 * @return the generated row as a String
	 */
	private <T> String generateTableRow(String rowIndex, T[] rowData) {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%" + Menu.COLUMN_WIDTH + "." + Menu.COLUMN_WIDTH + "s" + Menu.COLUMN_SEPARATOR, rowIndex + " "));
	    for (int i = 0; i < Menu.NUMBER_OF_COLUMNS; i++) {
	    	// TODO index out of bound?
	    	builder.append(String.format("%" + Menu.COLUMN_WIDTH + "." + Menu.COLUMN_WIDTH + "s" + Menu.COLUMN_SEPARATOR, rowData[i].toString() + " "));
	    }
	    return builder.toString();
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
