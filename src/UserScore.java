public class UserScore {
    private int score;
    private String userName;

    public UserScore(int score, String userName) {
        this.score = score;
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
