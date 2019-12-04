import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

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
	private final Scanner in;

	/**
	 * The number of rounds the players played.
	 */
	private int rounds;

    /**
     * The first player;
     */
	private final Player player1;

    /**
     * The second player
     */
	private final Player player2;
	
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

		// Ask a name from the human player.
        String name;
        while (true) {
            System.out.println("What is your name?");
            System.out.println(Menu.LINE_SEPARATOR);
            name = in.nextLine();
            if (!name.isBlank()) break;
            System.out.println(Menu.LINE_SEPARATOR);
        }
        System.out.println(Menu.LINE_SEPARATOR);
        System.out.println("Welcome, " + name + "!");

		player2 = new Player(name);

        activePlayer = player1;
        passivePlayer = player2;

        // Randomize who starts
        if ((new Random()).nextInt(2) == 0) {
            switchPlayers();
        }
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
			// Save the game at every start of the turns.
			if (menu.saveGame()) {
				System.out.println("The game is saved. You can exit to the Main Menu by typing in 'Exit'");
			} else {
				System.out.println("TODO: what if save unsuccessful?");
			}

			displayGrids();

			Position target;
			boolean valid = false;
			// Loop: get a valid target or input from the player which hasn't been fired upon.
			while (!valid) {
				// Try getting a valid target/input from the user (e.g. B5).
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
				// If the player is not an AI and already fired upon the target warn them about it.
				if (!valid && !(activePlayer instanceof AI)) {
					displayGrids();
					System.out.println("You have already fired on that target. Choose another one!");
				}
			}
		}
		endGame();

	}

	/**
	 * Displays the battlefields to the user(s).
	 */
	public void displayGrids() {
		// TODO add argument who should be displayed first
		// Numbers which help positioning the labels above the tables to the center.
	    final int spaceTillFirstLabel = (int) Math.ceil((Menu.TABLE_WIDTH - passivePlayer.getName().length()) / 2f);
	    final int firstPosition = spaceTillFirstLabel + passivePlayer.getName().length();
	    final int spaceTillSecondLabel = (int) Math.ceil((Menu.TABLE_WIDTH - activePlayer.getName().length()) / 2f);
	    final int secondPosition = (Menu.TABLE_WIDTH - firstPosition + Menu.GAP + spaceTillSecondLabel + activePlayer.getName().length());

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
				activePlayer.increaseMisses();
				rounds++;
				switchPlayers();
				break;
			// Hit
			case 1:
				activePlayer.increaseHits();
				break;
			// Sank
			case 2:
				activePlayer.increaseHits();
				// If all the ships sank in the fleet, end the game.
				if (passivePlayer.fleet.size() == 0) {
					end = true;
				}
				break;

			// The target is already fired upon
			case -1:
			// Fire cannot been executed
			default:
				return false;
		}
		return true; // The fire is done, return true
	}

	/**
	 * Switch the active and passive player of the game.
	 */
	private void switchPlayers() {
		Player holder = activePlayer;
		activePlayer = passivePlayer;
		passivePlayer = holder;
	}

	/**
	 * Wrap up the game at the end. Saving the score of the winner, delete the saved game.
	 */
	private void endGame() {
		// Display the final state of the game
		displayGrids();
		// Saving score for the winner.
		int score = calculateScore();
		System.out.printf("%s scored %d points.\n", activePlayer.getName(), score);
		while (true) {
			boolean success = menu.saveScore(new Score(activePlayer.getName(), score));
			if (!success) {
				System.out.println(Menu.LINE_SEPARATOR);
				System.out.println("Save was unsuccessful. Would you like to try again? (y/n)");
				String command = in.nextLine();
				if (command.toLowerCase().equals("y")) continue;
			}
			break;
		}
		menu.deleteSavedGame();
		System.out.println("TODO: what if delete unsuccessful?");
	}

	/**
	 * Calculate the score of the winner.
	 *
	 * @return the calculated score.
	 */
	private int calculateScore() {
		int rounds = (this.rounds % 2 == 1) ?
				(int) Math.ceil((float) this.rounds / 2) : // The player started the game
				this.rounds / 2; // The player was the second player
		return (int) ((activePlayer.getHits() * 5 - activePlayer.getMisses() * 1.25) * 1000 / rounds);
	}
}
