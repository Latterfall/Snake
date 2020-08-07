package game.model;

public class UserScore {
    private final int score;
    private final String userName;

    public UserScore(String userName, int score) {
        this.score = score;
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public String getUserName() {
        return userName;
    }
}
