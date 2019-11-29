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

	private boolean end;

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
		player1 = new AI();
		player2 = new AI();
		activePlayer = player1;
		passivePlayer = player2;
		end = false;
	}
	
	/**
	 * The whole cycle of an entire game, from placing the ships to winning the game.
	 */
	public void play() {
		// Placing the ships.
		activePlayer.placeShips(this, in);
		passivePlayer.placeShips(this, in);
		
		// Loop: Firing on each other.
		while (!end) {
			menu.saveGame();
			System.out.println("TODO: what if save unsuccessful?");
			System.out.println("The game is saved. You can exit to the Main Menu by typing in 'Exit'");

			displayGrids();

			Position target;
			boolean valid = false;
			// Loop: get a valid target or input from the player
			while (!valid) {
				// Try getting a valid target/input from the user.
				try {
					target = activePlayer.askCoordinate(in);
				} catch (InputMismatchException e) {
					// Exit the game if the user would like to.
					if (e.getMessage().equals("Exit")) return;
					else {
						// Continue asking a valid input from the user
						displayGrids();
					    System.out.println("Not a valid target. (For exit, type in 'Exit')");
                        continue;
                    }
				}

				valid = fire(target);
				if (!valid) displayGrids();
			}
		}
		
		// Wrap up the game in the end (saving score, delete saved game).
		System.out.println("TODO: calculate score for winner");
		menu.deleteSavedGame();
		System.out.println("TODO: what if delete unsuccessful?");
	}

	/**
	 * Displays the battlefields to the user(s).
	 */
	public void displayGrids() {
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
        			generateTableRow(String.valueOf(row + 1), passivePlayer.getBattlefieldData(row))
					+ " ".repeat(Menu.GAP)
					+ generateTableRow(String.valueOf(row + 1), activePlayer.getBattlefieldData(row))
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
		String lineSeparator = "——————";
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

		builder.append(generateTableCell(rowIndex));
	    for (int i = 0; i < Menu.NUMBER_OF_COLUMNS; i++) {
	    	builder.append(generateTableCell(rowData[i]));
	    }
	    return builder.toString();
	}

	/**
	 * Generate a cell for a table of battleship.
	 *
	 * @param content to display in the cell.
	 * @param <T> The type of the data to display.
	 * @return the generated cell as a String.
	 */
	private <T> String generateTableCell(T content) {
		return String.format(
				"%" + Menu.COLUMN_WIDTH + "." + Menu.COLUMN_WIDTH + "s" + Menu.COLUMN_SEPARATOR,
				content + " ".repeat((int) Math.ceil(Menu.COLUMN_WIDTH / 2f) - 1)
		);
	}

	/**
	 * Fires on the passive player with the given coordinates.
	 *
	 * @param target to fire upon.
	 * @return true if the fire was successful. False if there was a problem.
	 */
	private boolean fire(Position target) {
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
				in.nextLine();
				break;

			// The target is already fired upon
			case -1:
				System.out.println("TODO: Indicate to a human player that the target is already fired upon.");
			default:
				return false; // Fire cannot been executed
		}
		return true; // The fire is done, return true
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
