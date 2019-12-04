import java.io.Serializable;

public class Score implements Serializable {

    private final String name;

    private final int score;
    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
