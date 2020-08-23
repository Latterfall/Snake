package game.logic;

import game.model.Direction;
import game.model.Snake;
import game.model.UserScore;
import game.panels.GamePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static game.model.Direction.*;

public class GameLogicManager implements ActionListener {
    // Game variables and properties

    private GamePanel gamePanel;
    private final GameSettings gameSettings;

    private int scoreCounter;

    private boolean isSnakeBodyNeededToBeIncreased;
    private boolean isInGame;
    private boolean isGamePaused;
    private boolean isFirstGame;
    private boolean isNeedToAskSaveScore;

    private Point applePosition;
    private final Timer timer;
    private Snake snake;

    // Constructors

    public GameLogicManager(GameSettings gameSettings) {
        this.gameSettings = gameSettings;

        isSnakeBodyNeededToBeIncreased = false;
        isInGame = false;
        isGamePaused = false;
        isFirstGame = true;
        isNeedToAskSaveScore = true;

        timer = new Timer(gameSettings.getGameSpeed(), this);
    }

    // Getters

    public int getScoreCounter() {
        return scoreCounter;
    }

    public boolean isSnakeBodyNeededToBeIncreased() {
        return isSnakeBodyNeededToBeIncreased;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public boolean isGamePaused() {
        return isGamePaused;
    }

    public boolean isFirstGame() {
        return isFirstGame;
    }

    public boolean isNeedToAskSaveScore() {
        return isNeedToAskSaveScore;
    }

    public Point getApplePosition() {
        return applePosition;
    }

    public Snake getSnake() {
        return snake;
    }

    // Setters

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setGameSpeed(int gameSpeed) {
        timer.setDelay(gameSpeed);
    }

    // Logic methods

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (isInGame) {
            if (isGamePaused) {
                timer.stop();
            }
            checkIsAppleEaten();
            checkSnakeNotDead();
            moveSnake();
        } else {
            if (isNeedToAskSaveScore) {
                saveResult();
            }
        }
        gamePanel.repaint();
    }

    public void startGame() {
        scoreCounter = 0;
        isFirstGame = false;
        isInGame = true;
        isNeedToAskSaveScore = true;
        snake = new Snake();
        locateApplePosition();
        locateSnakeInitialPosition();
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
    }

    public void pauseGame() {
        if (isInGame) {
            if (isGamePaused) {
                isGamePaused = false;
                timer.start();
            } else {
                isGamePaused = true;
            }
        }
    }

    private void printScore() {
        scoreCounter++;
        //gamePanel.scoreLabel.setText("Your score: " + scoreCounter);
    }

    private void moveSnake() {
        // Determines snake head new position, depending on current snake movement direction.
        Point snakeHeadNewPoint = new Point();
        Point snakeHeadCurrentPoint = snake.snakeBodyPoints.getFirst();
        int cellSize = gameSettings.getCellSize();
        switch (snake.currentSnakeDirection) {
            case RIGHT:
                snakeHeadNewPoint.x = checkSnakePointCoordinateToBeInBounds(snakeHeadCurrentPoint.x + cellSize);
                snakeHeadNewPoint.y = snakeHeadCurrentPoint.y;
                break;
            case LEFT:
                snakeHeadNewPoint.x = checkSnakePointCoordinateToBeInBounds(snakeHeadCurrentPoint.x - cellSize);
                snakeHeadNewPoint.y = snakeHeadCurrentPoint.y;
                break;
            case UP:
                snakeHeadNewPoint.x = snakeHeadCurrentPoint.x;
                snakeHeadNewPoint.y = checkSnakePointCoordinateToBeInBounds(snakeHeadCurrentPoint.y - cellSize);
                break;
            case DOWN:
                snakeHeadNewPoint.x = snakeHeadCurrentPoint.x;
                snakeHeadNewPoint.y = checkSnakePointCoordinateToBeInBounds(snakeHeadCurrentPoint.y + cellSize);
                break;
        }

        // Moves all points of snake from position i to position i + 1, to the first position placed snake's head new point.
        // If there is no need to add new point to snake's body, point in last position removes.
        snake.snakeBodyPoints.addFirst(snakeHeadNewPoint);
        if (!isSnakeBodyNeededToBeIncreased) {
            snake.snakeBodyPoints.removeLast();
        } else {
            isSnakeBodyNeededToBeIncreased = false;
        }
    }

    /*
    private void placeWall() {
        for (Point point : wall) {
            point.x *= cellSize;
            point.y *= cellSize;
        }
    }
    */

    // Logic methods - Location methods

    private void locateApplePosition() {
        applePosition = locateRandomPositionPoint();
    }

    private void locateSnakeInitialPosition() {
        Point snakeInitialPositionPoint = locateRandomPositionPoint();
        snake.snakeBodyPoints.add(snakeInitialPositionPoint);

        int snakeInitialDirection = (int) (Math.random() * 4);
        switch (snakeInitialDirection) {
            case 0:
                snake.currentSnakeDirection = Direction.UP;
                break;
            case 1:
                snake.currentSnakeDirection = Direction.DOWN;
                break;
            case 2:
                snake.currentSnakeDirection = Direction.LEFT;
                break;
            case 3:
                snake.currentSnakeDirection = RIGHT;
                break;
        }

        for (int i = 1; i < gameSettings.getSnakeInitialSize(); i++) {
            int cellSize = gameSettings.getCellSize();

            Point snakeBodyPoint = new Point();
            Point snakeHeadPoint = snake.snakeBodyPoints.getFirst();
            if (snake.currentSnakeDirection.equals(Direction.UP)) {
                snakeBodyPoint.x = snakeHeadPoint.x;
                snakeBodyPoint.y = checkSnakePointCoordinateToBeInBounds(snakeHeadPoint.y + i * cellSize);
            } else if (snake.currentSnakeDirection.equals(Direction.DOWN)) {
                snakeBodyPoint.x = snakeHeadPoint.x;
                snakeBodyPoint.y = checkSnakePointCoordinateToBeInBounds(snakeHeadPoint.y - i * cellSize);
            } else if (snake.currentSnakeDirection.equals(Direction.LEFT)) {
                snakeBodyPoint.x = checkSnakePointCoordinateToBeInBounds(snakeHeadPoint.x + i * cellSize);
                snakeBodyPoint.y = snakeHeadPoint.y;
            } else {
                snakeBodyPoint.x = checkSnakePointCoordinateToBeInBounds(snakeHeadPoint.x - i * cellSize);
                snakeBodyPoint.y = snakeHeadPoint.y;
            }
            snake.snakeBodyPoints.add(snakeBodyPoint);
        }
    }

    private Point locateRandomPositionPoint() {
        int cellSize = gameSettings.getCellSize();
        int numberOfCells = gameSettings.getNumberOfCells();

        Point randomPositionPoint = new Point();
        randomPositionPoint.x = (int) (Math.random() * (numberOfCells)) * cellSize;
        randomPositionPoint.y = (int) (Math.random() * (numberOfCells)) * cellSize;
        return randomPositionPoint;
    }

    // Logic methods - Checking methods

    private void checkIsAppleEaten() {
        for (Point point : snake.snakeBodyPoints) {
            if (point != null) {
                if (point.x == applePosition.x && point.y == applePosition.y) {
                    locateApplePosition();
                    isSnakeBodyNeededToBeIncreased = true;
                    printScore();
                }
            }
        }
    }

    private void checkSnakeNotDead() {
        Point snakeHeadPoint = snake.snakeBodyPoints.getFirst();
        snake.snakeBodyPoints.forEach(point -> {
            if (point != snakeHeadPoint && point.equals(snakeHeadPoint)) {
                isInGame = false;
            }
        });
    }

    private int checkSnakePointCoordinateToBeInBounds(int pointCoordinate) {
        if (pointCoordinate == gameSettings.getGameFieldSideSize()) {
            return pointCoordinate - gameSettings.getGameFieldSideSize();
        } else if (pointCoordinate < 0) {
            return pointCoordinate + gameSettings.getGameFieldSideSize();
        }
        return pointCoordinate;
    }

    private void saveResult() {
        isNeedToAskSaveScore = false;
        int answer = JOptionPane.showConfirmDialog(
                gamePanel,
                "Want to save your score?",
                "Saving score",
                JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            String userName = JOptionPane.showInputDialog(
                    gamePanel,
                    "Enter your name",
                    "Enter your name",
                    JOptionPane.PLAIN_MESSAGE);
            if (userName.isEmpty()) {
                userName = "Unnamed player";
            }
            ScoreFileManager scoreFileManager = ScoreFileManager.getInstance();
            scoreFileManager.saveScore(new UserScore(userName, scoreCounter));
        }
    }

}
