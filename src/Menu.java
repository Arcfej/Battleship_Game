import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * The main menu and entry point of the game. It handles all the interactions between the user(s) and the computer.
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

	/**
	 * The title of the game line by line.
	 */
	private static final String[] GAME_TITLE = {
			"  __ )          |    |    |              |     _)              \n",
			"  __ \\    _` |  __|  __|  |   _ \\   __|  __ \\   |  __ \\    __| \n",
			"  |   |  (   |  |    |    |   __/ \\__ \\  | | |  |  |   | \\__ \\ \n",
			" ____/  \\__,_| \\__| \\__| _| \\___| ____/ _| |_| _|  .__/  ____/ \n",
			"                                                  _|           \n"
	};

	/**
	 * The space from the start of the line till the start of the title.
	 */
	private static final int SPACE_TILL_TITLE = (int) Math.floor((LINE_SEPARATOR.length() - GAME_TITLE[0].length()) / 2f);

	/**
	 * The character which separates the columns of a battlefield.
	 */
	public static final char COLUMN_SEPARATOR = '|';

	/**
	 * The character which is used to display the missed shots.
	 */
	public static final char MISSED_SHOT = '—';

	/**
	 * The character which is used to display the hits on a ship.
	 */
	public static final char HIT = 'X';

	/**
	 * The character which is used to display the intact ship parts if they are visible.
	 */
	public static final char SHIP = 'O';

	/**
	 * The character which is used to display the sank ships.
	 */
	public static final char SANK_SHIP = '#';

	/**
	 * The file path to the saved game(s).
	 */
	private static final Path SAVE_PATH = Paths.get(".save/save01.save");

	/**
	 * The file path to the top scores.
	 */
	private static final Path SCORES_PATH = Paths.get(".scores.txt");

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
	 *
	 * @param args The arguments the game starts with.
	 */
	public static void main(String[] args) {
		Menu menu = new Menu();

		Scanner in = new Scanner(System.in);

		// Show the menu until the user exits the program in displayMenu()
		boolean exit = false;
		while (!exit) {
            System.out.println();

            // Display the game title
			System.out.println(LINE_SEPARATOR);
			System.out.println();
            for (String line : GAME_TITLE) {
                System.out.printf("%" + (SPACE_TILL_TITLE + line.length()) + "s", line);
            }
			System.out.println(LINE_SEPARATOR);
			System.out.println();

			exit = menu.displayMenu(in);
		}
	}
	
	/**
	 * Display the Main Menu to the user. It contains several options, all of them starts a game function.
	 * One of them is to exit the program.
	 * 
	 * @param in The input stream through the user communicates with the program.
	 * @return true if the user wants to exit the program.
	 */
	private boolean displayMenu(Scanner in) {
		boolean error = false;
		// Display the menu until there is an error with the input. Valid input will call return in the loop.
		while (true) {
			// Display an error message if there was an error.
			if (error) System.out.println("Valid inputs are numbers listed bellow.");

			// Display the menu options
			System.out.println();
			System.out.println("Choose one of the options:");
			System.out.println(LINE_SEPARATOR);
			System.out.println("1) New Game");
			// Display the Load Game option only if there is a saved game.
			if (hasSavedGame) System.out.println("2) Load Game");
			System.out.println("3) Leader board");
			System.out.println("0) Exit");
			System.out.println(LINE_SEPARATOR);

			// Validate the input from the user.
			error = false; // Assume there won't be any error
			if (in.hasNextInt()) {
				int command = in.nextInt();
				in.nextLine(); // Take the scanner to the next line after reading the number.

                System.out.println(LINE_SEPARATOR);

				switch (command) {
					// Exit the game
					case 0:
						return true;
					// Start a new game
					case 1:
						newGame(in);
						return  false;
					// Show the leader board
					case 3:
						showLeaderBoard();
						return false;
					// Load the saved game if there is any
					case 2:
						if (hasSavedGame) {
							loadGame(in);
							return false;
						}
					// Display error message in any other case
					default:
						error = true;
				}
			} else {
				in.nextLine();
				error = true;
			}
			System.out.println(LINE_SEPARATOR);
		}
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
        if (!Files.isReadable(SAVE_PATH)) {
            System.out.println("Save don't exists");
            return;
        }
        ObjectInputStream savedState = null;

        try {
            // Open the file
            savedState = new ObjectInputStream(new FileInputStream(SAVE_PATH.toFile()));
            List<Object> state = new ArrayList<>(((List<Serializable>) savedState.readObject()));
            // Restore the previous game
            GameOfBattleships game = GameOfBattleships.restorePreviousGame(this, in, state);
            game.play();
        }

        // Catch the errors
        catch (IOException | ClassCastException e) {
            System.err.println("The saved scores are corrupt:\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Close the file
        finally {
            if (savedState != null) {
                try {
                    savedState.close();
                } catch (IOException e) {
                    System.err.println("Error while closing file: " + e.getMessage());
                }
            }
        }
	}
	
	/**
	 * Shows the leader board with the top 10 scores from the previous plays.
	 */
	private void showLeaderBoard() {
		List<Score> leaderBoard;
		if (Files.exists(SCORES_PATH)) {
			leaderBoard = readScores();
			if (leaderBoard != null) {
				for (Score score : leaderBoard) {
					System.out.println(score.getName() + ": " + score.getScore());
				}
			}
		}
	}
	
	/**
	 * Saves the current state of the game.
	 *
     * @param state The state of the game to save.
	 * @return if the save was successful or not.
	 */
	public boolean saveGame(List<Serializable> state) {
		if (!Files.exists(SAVE_PATH)) {
			SAVE_PATH.toFile().getParentFile().mkdirs();
		}
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(SAVE_PATH.toFile()))) {
            // Save the file
            writer.writeObject(state);
            // Return true if there wasn't any error.
            hasSavedGame = true;
            return true;
        } catch (NotSerializableException e) {
            System.out.println("The state for save is not serializable.");
        } catch (FileNotFoundException | SecurityException e) {
            System.err.println("Access denied: " + e.getMessage());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
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
	 * Try to save the a score with the given details to the Leader board.
	 * The Leader board contains only the top 10 scores. If the new score is not in this
	 * 
	 * @param newScore The new score to save.
	 * @return 	Return false if the System cannot save the score because of some error.
	 * 			Return true in any other case, even if the score is not in the top 10.
	 */
	public boolean saveScore(Score newScore) {
		List<Score> leaderBoard;
		// Load the Leader board
		if (Files.exists(SCORES_PATH)) {
			leaderBoard = readScores();
			if (leaderBoard == null) {
				// Leader board couldn't been loaded
				return false;
			}
		}
		// If the Leader board not yet exist, create a new one
		else {
			leaderBoard = new ArrayList<>(10);
		}

		// If the Leader board is empty or the new score is in the top 10, save it.
		if (leaderBoard.size() == 0 || leaderBoard.size() < 10 || leaderBoard.get(leaderBoard.size() - 1).getScore() < newScore.getScore()) {
			leaderBoard.add(newScore);
			leaderBoard = leaderBoard.stream()
					.sorted(Comparator.comparingInt(Score::getScore).reversed()) // Sort the scores in descending order
					.limit(10) // Save only the top 10 scores
					.collect(Collectors.toList());
			try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(SCORES_PATH.toFile()))) {
				// Save the file
				writer.writeObject(leaderBoard);
			} catch (FileNotFoundException | SecurityException e) {
				System.out.println("Access denied: " + e.getMessage());
				return false;
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
		}
		// Return true if there wasn't any error.
		return true;
	}

	/**
	 * Load the leader board from the disk.
	 *
	 * @return the leader board as a list.
	 */
	private List<Score> readScores() {
		if (!Files.isReadable(SCORES_PATH)) {
			return null;
		}
		ObjectInputStream leaderBoard = null;

		try {
			// Open the file
			leaderBoard = new ObjectInputStream(new FileInputStream(SCORES_PATH.toFile()));

			return ((List<Score>) leaderBoard.readObject()).stream()
					.sorted(Comparator.comparingInt(Score::getScore).reversed())
					.collect(Collectors.toList());
		}

		// Catch the errors
		catch (IOException | ClassCastException e) {
			System.err.println("The saved scores are corrupt:\n" + e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// Close the file
		finally {
			if (leaderBoard != null) {
				try {
					leaderBoard.close();
				} catch (IOException e) {
					System.err.println("Error while closing file: " + e.getMessage());
				}
			}
		}
		// Return null if some error occurred.
		return null;
	}
}
