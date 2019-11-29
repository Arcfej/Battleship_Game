import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The main menu and entry point of the Battleship game.
 * It handles all the interactions between the user(s) and the computer.
 * 
 * @author MIklosMayer
 */
public class Menu {

	/**
	 * The number of rows of a battleship table.
	 */
	public static final int NUMBER_OF_ROWS = 10;

	/**
	 * The number of Columns of a battleship table.
	 */
	public static final int NUMBER_OF_COLUMNS = 10;

	/**
	 * The width of one column of the battlefield without the borders. (characters)
	 */
	public static final int COLUMN_WIDTH = 5;
	
	/**
	 * The gap between the two displayed battlefield.
	 */
	public static final int GAP = 10;
	
	/**
	 * The width of one table (battlefield).
	 */
	public static final int TABLE_WIDTH = (NUMBER_OF_COLUMNS + 1) * (COLUMN_WIDTH + 1);
	
	/**
	 * Used between lines to separate contents in the console.
	 */
	public static final String LINE_SEPARATOR = String.format("%" + (TABLE_WIDTH * 2 + GAP) + "s", "").replace(" ", "—");

	private static final String GAME_TITLE = "GAME OF BATTLESHIPS";
	private static final int SPACE_TILL_TITLE = (int) Math.ceil((LINE_SEPARATOR.length() - GAME_TITLE.length()) / 2f);

	/**
	 * The character which separates the columns of a battlefield.
	 */
	public static final char COLUMN_SEPARATOR = '|';
	
	/**
	 * The character which is used to display the missed shots.
	 */
	public static final char MISSED_SHOT = '•';
	
	/**
	 * The character which is used to display the hits on a ship.
	 */
	public static final char HIT = 'Ø';
	
	/**
	 * The character which is used to display the unhit ships if they are visible.
	 */
	public static final char SHIP = 'O';
	
	/**
	 * The character which is used to display the sank ships.
	 */
	public static final char SANK_SHIP = '#';

	/**
	 * The file path to the saved game(s).
	 */
	private static final Path SAVE_PATH = Paths.get("/save/save01.save");
	
	/**
	 * The current game.
	 */
	private GameOfBattleships game;
	
	/**
	 * True if there is a saved game on the drive.
	 */
	private boolean hasSavedGame;

	/**
	 * The default constructor of the class
	 */
	private Menu() {
		hasSavedGame = Files.exists(SAVE_PATH);
		game = null;
	}

	/**
	 * The entry point of the game.
	 * @param args
	 */
	public static void main(String[] args) {
		Menu menu = new Menu();

		Scanner in = new Scanner(System.in);

		// Show the menu until the user exits the program in displayMenu()
		menu.displayMenu(in);
	}
	
	/**
	 * Display the Main Menu to the user. It contains several options, all of them starts a game function.
	 * One of them is to exit the program.
	 * 
	 * @param in The input stream through the user communicates with the program.
	 */
	private void displayMenu(Scanner in) {
		boolean exit = false;
		while (!exit) {
			System.out.println();
			System.out.println(LINE_SEPARATOR);
			System.out.printf("%" + (SPACE_TILL_TITLE + GAME_TITLE.length()) + "s\n", GAME_TITLE);
			System.out.println(LINE_SEPARATOR);
			// Display the menu options
			System.out.println();
			System.out.println("Choose one of the options:");
			System.out.println(LINE_SEPARATOR);
			System.out.println("1) New Game");
			// Display the Load Game option only if there is a saved game.
			if (hasSavedGame) {
				System.out.println("2) Load Game");
			}
			System.out.println("3) Leaderboard");
			System.out.println("0) Exit");
			System.out.println(LINE_SEPARATOR);

			// Validate the input from the user.
			if (in.hasNextInt()) {
				int command = in.nextInt();
				// Take the scanner to the next line after reading the number.
				in.nextLine();
				System.out.println(LINE_SEPARATOR);
				switch (command) {
					// Exit the game
					case 0:
						exit = true;
						break;
					// Start a new game
					case 1:
						newGame(in);
						break;
					// Show the leaderboard
					case 3:
						showLeaderboard();
						break;
					// Load the saved game if there is any
					case 2:
						if (hasSavedGame) {
							loadGame(in);
							break;
						}
					// Display error message in any other case
					default:
						invalidMenuChoice();
				}
			} else {
				in.nextLine();
				invalidMenuChoice();
			}
		}
	}
	
	/**
	 * Called when the user entered an invalid command in the Main Menu.
	 * Displays an error message to the user.
	 */
	private void invalidMenuChoice() {
		System.out.println("Valid inputs are numbers listed bellow.");
	}
	
	/**
	 * Starts a new game.
	 * 
	 * @param in The input stream through the user communicates with the program.
	 */
	private void newGame(Scanner in) {
		game = new GameOfBattleships(this, in);
		game.play();
	}
	
	/**
	 * Loads an existing saved game from the disk.
	 * 
	 * @param in The input stream through the user communicates with the program.
	 */
	private void loadGame(Scanner in) {
		System.out.println("Load game chosen");
	}
	
	/**
	 * Shows the leaderboard with the top 10 scores from the previous plays.
	 */
	private void showLeaderboard() {
		System.out.println("Show leaderboard chosen");
	}
	
	/**
	 * Saves the current state of the game.
	 * 
	 * @return if the save was successful or not.
	 */
	public boolean saveGame() {
		System.out.println("TODO: save the game");
		return false;
	}
	
	/**
	 * Deletes the saved game.
	 * 
	 * @return if the deletion was successful or not.
	 */
	public boolean deleteSavedGame() {
		System.out.println("TODO: delete the saved game");
		return false;
	}
	
	/**
	 * Saves the score with the given details.
	 * 
	 * @param score The score of the player
	 * @param name The name of the player
	 * @return if the save was successful or not.
	 */
	public boolean saveScore(int score, String name) {
		System.out.println("TODO: save current score");
		return false;
	}
}
