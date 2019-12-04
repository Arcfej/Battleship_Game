import java.io.Serializable;

/**
 * Represents a final score of a player.
 */
public class Score implements Serializable {

    /**
     * The name of the player.
     */
    private final String name;

    /**
     * The final score of the player.
     */
    private final int score;

    /**
     * The default constructor of the player.
     *
     * @param name The name of the player.
     * @param score The final score of the player.
     */
    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Return the name of the player.
     *
     * @return the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the final score of the player.
     *
     * @return the final score of the player.
     */
    public int getScore() {
        return score;
    }
}
