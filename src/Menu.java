import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The main menu and entry point of the game. It handles all the interactions between the user(s) and the computer.
 * 
 * @author MIklosMayer
 */
public class Menu {
	
	public static final String SEPARATOR = "—————————————————————————————————————————————————————————————";

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
		while (true) {
			menu.displayMenu(in);
		}
	}
	
	/**
	 * Display the Main Menu to the user. It contains several options, all of them starts a game function.
	 * One of them is to exit the program.
	 * 
	 * @param in The input stream through the user communicates with the program.
	 */
	private void displayMenu(Scanner in) {
		// Display the menu options
		System.out.println();
		System.out.println("Choose one of the options:");
		System.out.println(SEPARATOR);
		System.out.println("1) New Game");
		// Display the Load Game option only if there is a saved game.
		if (hasSavedGame) {
			System.out.println("2) Load Game");
		}
		System.out.println("3) Leaderboard");
		System.out.println("0) Exit");
		System.out.println(SEPARATOR);
		
		// Validate the input from the user.
		if (in.hasNextInt()) {
			int command = in.nextInt();
			// Take the scanner to the next line after reading the number.
			in.nextLine();
			switch (command) {
				// Exit the game
				case 0:
					System.exit(0);
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
	
	/**
	 * Called when the user entered an invalid command in the Main Menu.
	 * Displays an error message to the user.
	 */
	private void invalidMenuChoice() {
		System.out.println();
		System.out.println(SEPARATOR);
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
