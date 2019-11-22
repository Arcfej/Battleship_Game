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
		
		// Firing on eachother.
		boolean end = false;
		while (!end) {
			menu.saveGame();
			System.out.println("TODO: what if save unsuccessful?");
			Position target;
			while (true) {
				target = askCoordinate();
				if (target != null) {
					System.out.println("TODO: check if the target is previously fired upon");
					break;
				}
			}
			switch(passivePlayer.takeFire(target)) {
				// Missed
				case 0:
					System.out.println("TODO: increase misses");
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
	 */
	private Position askCoordinate() {
		System.out.println("What is your target?");
		System.out.println(Menu.SEPARATOR);
		in.next();
		System.out.println("TODO: validate target");
		return new Position();
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
	public void switchPlayer() {
		Player holder = activePlayer;
		activePlayer = passivePlayer;
		passivePlayer = holder;
	}
}
