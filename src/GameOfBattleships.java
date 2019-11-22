import java.util.Scanner;

public class GameOfBattleships {
	
	private final Menu menu;
	private Scanner in;
	private int rounds;
	private Player activePlayer;
	private Player passivePlayer;

	public GameOfBattleships(Menu menu, Scanner in) {
		this.menu = menu;
		this.in = in;
		rounds = 1;
		activePlayer = new Player();
		passivePlayer = new Player();
	}
	
	public void play() {
		activePlayer.placeShips();
		passivePlayer.placeShips();
		
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
		
		System.out.println("TODO: calculate score for winner");
		menu.deleteSavedGame();
		System.out.println("TODO: what if delete unsuccessful?");
	}
	
	private Position askCoordinate() {
		System.out.println("What is your target?");
		System.out.println(Menu.SEPARATOR);
		in.next();
		System.out.println("TODO: validate target");
		return new Position();
	}

	private void displayGrids() {
		System.out.println("TODO: display the battlefield");
	}
	
	private void endGame() {
		System.out.println("TODO: end game");
	}
	
	public int fire(Position position) {
		System.out.println("TODO: implement fire in GameOfBattleships");
		return -1;
	}
	
	public int getRounds() {
		return rounds;
	}
	
	public void switchPlayer() {
		Player holder = activePlayer;
		activePlayer = passivePlayer;
		passivePlayer = holder;
	}
}
