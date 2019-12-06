import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
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
	private final Scanner in;

	/**
	 * The number of rounds the players played.
	 */
	private int rounds;
	
	/**
	 * The player who is firing on the other.
	 */
	private Player activePlayer;
	
	/**
	 * The player who is taking the fire. 
	 */
	private Player passivePlayer;

    /**
     * True if the ships have already been placed on the battlefields.
     */
    private boolean shipsPlaced;

	/**
	 * True if the play ended. (Some of the player sank all the ships of the other.)
	 */
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
		activePlayer = new AI();

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
		passivePlayer = new Player(name);

        // Randomize who starts
        if ((new Random()).nextInt(2) == 0) {
            switchPlayers();
        }
        shipsPlaced = false;
		end = false;
	}

    /**
     * Private constructor to use only for restoring a previous game.
     *
     * @param menu The menu of the game which handles e.g. the saving of the game state.
     * @param in The input stream through the user communicates with the program.
     * @param rounds The number of rounds the players played.
     * @param activePlayer The player who is firing on the other.
     * @param passivePlayer The player who is taking the fire.
     */
	private GameOfBattleships(Menu menu, Scanner in, int rounds, Player activePlayer, Player passivePlayer) {
		this.menu = menu;
		this.in = in;
		this.rounds = rounds;
		this.activePlayer = activePlayer;
		this.passivePlayer = passivePlayer;
		shipsPlaced = true;
		end = false;
	}

    /**
     * Restore a previous game from the given state.
     *
     * @param menu The menu of the game which handles e.g. the saving of the game state.
     * @param in The input stream through the user communicates with the program.
     * @param state The state of the previous game.
     *              The first element of the list is the number of rounds.
     *              The second element of the list is the player who has the turn.
     *              The third element of the list is the player who waiting for its turn.
     * @return the restored game.
     * @throws IllegalArgumentException if the game cannot been restored from the provided state.
     */
	public static GameOfBattleships restorePreviousGame(Menu menu, Scanner in, List<Object> state)
			throws IllegalArgumentException {
		int rounds;
		Player activePlayer;
		Player passivePlayer;
		try {
			rounds = (int) state.get(0);
			activePlayer = (Player) state.get(1);
			passivePlayer = (Player) state.get(2);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Error: The saved game is corrupt.");
		}
		return new GameOfBattleships(menu, in, rounds, activePlayer, passivePlayer);
	}

	/**
	 * The whole cycle of an entire game, from placing the ships to winning the game.
	 */
	public void play() {
		if (!shipsPlaced) {
			// Placing the ships.
			activePlayer.placeShips(this, in);
			passivePlayer.placeShips(this, in);
			shipsPlaced = true;
		}

		// Loop: Firing on each other.
		while (!end) {
		    // Save the current state of the game
			if (!menu.saveGame(List.of(rounds, activePlayer, passivePlayer))) {
			    System.out.println("The game couldn't been saved. Exiting now will cause to lose the current game.");
            } else {
                System.out.println("The game is saved. You can exit to the Main Menu by typing in 'Exit'");
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
		displayTableHeads();

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
	 * Display the head of the battleships. Contains the name of the players and their scores.
	 */
	private void displayTableHeads() {
		// Numbers which help positioning the labels above the tables to the center.
		final int spaceTillFirstLabel = (int) Math.floor((Menu.TABLE_WIDTH - 2 - passivePlayer.getName().length()) / 2f);
		final int firstPosition = spaceTillFirstLabel + passivePlayer.getName().length();
		final int spaceTillSecondLabel = (int) Math.floor((Menu.TABLE_WIDTH - 2 - activePlayer.getName().length()) / 2f);
		final int secondPosition = spaceTillSecondLabel + activePlayer.getName().length();


		// Display the labels above the tables
		System.out.println();
		System.out.println(generateTableHeadsSeparator());
		System.out.printf(
				"|%" + firstPosition + "s" + " ".repeat(Menu.TABLE_WIDTH - firstPosition - 2) + "|" + " ".repeat(Menu.GAP) + "|%"+ secondPosition + "s" + " ".repeat(Menu.TABLE_WIDTH - secondPosition - 2) + "|\n",
				passivePlayer.getName().toUpperCase(),
				activePlayer.getName().toUpperCase()
		);

		System.out.println(generateTableHeadsSeparator());
		// Display the player's current scores
		System.out.print(generateHeaderRow(
				" Hits: " + passivePlayer.getHits(), " Misses: " + passivePlayer.getMisses())
				+ " ".repeat(Menu.GAP) +
				generateHeaderRow(" Hits: " + activePlayer.getHits(), " Misses: " + activePlayer.getMisses()) + "\n"
		);
		System.out.println(generateTableHeadsSeparator());
		System.out.println();
	}

	/**
	 * Generate a line separator for the heads of the two tables.
	 *
	 * @return the generated row separator as String
	 */
	private String generateTableHeadsSeparator() {
		return String.format("|%" + (Menu.TABLE_WIDTH - 2) + "." + (Menu.TABLE_WIDTH - 2) + "s|%" + Menu.GAP + "s|%" + (Menu.TABLE_WIDTH - 2) + "." + (Menu.TABLE_WIDTH - 2) + "s|",
				"—".repeat(Menu.TABLE_WIDTH),
				" ".repeat(Menu.GAP),
				"—".repeat(Menu.TABLE_WIDTH));
	}

	/**
	 * Generate a row for one table's head.
	 *
	 * @param firstCell The data to display in the first cell.
	 * @param secondCell The data to display in the second cell.
	 * @return the generated table row.
	 */
	private String generateHeaderRow(String firstCell, String secondCell) {
		return String.format(
				"|%-" + (int) (Menu.TABLE_WIDTH / 2f - 2) + "s|%-" + ((int) Math.ceil(Menu.TABLE_WIDTH / 2f) - 1) + "s|",
				firstCell,
				secondCell
		);
	}

	/**
	 * Generate a separator line between two rows of a battleship table
	 *
	 * @return the generated line as a String
	 */
	private String generateTableRowSeparator() {
		String lineSeparator = "—".repeat(Menu.COLUMN_WIDTH);
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
