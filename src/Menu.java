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
	
	private static final String SEPARATOR = "———————————————————————————————————————————————————";
	
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

	public Menu() {
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
		
		while (true) {
			valid = menu.displayMenu(in);
		}
	}
	
	private boolean displayMenu(Scanner in) {
		System.out.println();
		System.out.println("Choose one of the options:");
		System.out.println(SEPARATOR);
		System.out.println("1) New Game");
		if (hasSavedGame) {
			System.out.println("2) Load Game");
		}
		System.out.println("3) Leaderboard");
		System.out.println("0) Exit");
		System.out.println(SEPARATOR);
		System.out.println();
		
		if (in.hasNextInt()) {
			switch (in.nextInt()) {
				case 0:
					System.exit(0);
					break;
				case 1:
					newGame();
					break;
				case 3:
					showLeaderboard();
					break;
				case 2:
					if (hasSavedGame) {
						loadGame();
						break;
					}
				default:
					invalidMenuChoice();
					return false;
			}
		} else {
			in.next();
			invalidMenuChoice();
			return false;
		}
		return true;
	}
	
	private void invalidMenuChoice() {
		System.out.println();
		System.out.println(SEPARATOR);
		System.out.println("Valid inputs are numbers listed bellow.");
	}
	
	private void newGame() {
		game = new GameOfBattleships();
		System.out.println("New game chosen");
	}
	
	private void loadGame() {
		System.out.println("Load game chosen");
	}
	
	private void showLeaderboard() {
		System.out.println("Show leaderboard chosen");
	}
}
