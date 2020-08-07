package game.utils;

import game.model.UserScore;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreFileManager {
    private static ScoreFileManager scoreFileManager;
    private final String scoreFileName = "scores";
    private final Path topScoreFile = Paths.get(System.getProperty("user.dir") + "/" + scoreFileName);

    private ScoreFileManager() {
    }

    public static ScoreFileManager getInstance() {
        if (scoreFileManager == null) {
            scoreFileManager = new ScoreFileManager();
        }
        return scoreFileManager;
    }

    public void saveScore(UserScore userScore) {
        try {
            if (!Files.exists(topScoreFile)) {
                Files.createFile(topScoreFile);
            }
            String userNameAndScore = userScore.getUserName() + ":" + userScore.getScore() + "\n";
            Files.write(topScoreFile, userNameAndScore.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public List<UserScore> getAllScores() {
        ArrayList<UserScore> userScores = new ArrayList<>();

        if (Files.exists(topScoreFile)) {
            try {
                // Читаем все строки, разбиваем их на имя пользователя и его счёт, переводим эти данные в объекты game.model.UserScore.
                List<String> topScoreFileData = Files.readAllLines(topScoreFile);
                topScoreFileData.forEach(line -> {
                    String[] playerNameAndScore = line.split(":");
                    userScores.add(new UserScore(playerNameAndScore[0], Integer.parseInt(playerNameAndScore[1])));
                });

                // Описываем компаратор для сортировки по убыванию и на его основе сортируем коллекцию.
                Comparator<UserScore> scoreComparator = (o1, o2) -> Integer.compare(o2.getScore(), o1.getScore());
                userScores.sort(scoreComparator);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return userScores;
    }
}
