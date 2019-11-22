import java.util.Scanner;

public class Player {

	public Player() {
		// TODO Auto-generated constructor stub
	}
	
	public void placeShips() {
		System.out.println("TODO: place ships");
	}

	public int takeFire(Position target) {
		System.out.println("TODO: implement takeFire");
		Scanner in = new Scanner(System.in);
		System.out.println("0 - miss; 1 - hit; 2 - sink; -1 exit");
		if (in.hasNextInt()) {
			return in.nextInt();
		} else {
			in.next();
		}
		return -1;
	}
}
