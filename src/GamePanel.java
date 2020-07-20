import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class GamePanel extends JPanel implements ActionListener {
    // Game variables and parameters

    private final int cellSize = 10;
    private final int snakeInitialSize = 3;
    private int numberOfCells = 40;
    private int gameSpeed = 100;
    private int scoreCounter;

    private Point[] snake;
    private Point applePosition = new Point();
    private final JLabel scoreLabel = new JLabel("Your score: " + scoreCounter);
    private final Timer timer = new Timer(gameSpeed, this);

    private boolean isSnakeBodyNeededToBeIncreased = false;
    private boolean inGame = false;
    private boolean isGamePaused = false;
    private boolean isFirstGame = true;
    private boolean isNeedToAskSaveScore = true;

    private Color appleColor;
    private Color snakeHeadColor;
    private Color snakeBodyColor;
    private Color textColor;

    public enum Theme {
        DARK, LIGHT
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    private Direction currentSnakeDirection;

    private class SnakeKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_LEFT && (!currentSnakeDirection.equals(Direction.RIGHT))){
                currentSnakeDirection = Direction.LEFT;
            }

            if(key == KeyEvent.VK_RIGHT && (!currentSnakeDirection.equals(Direction.LEFT))){
                currentSnakeDirection = Direction.RIGHT;
            }

            if(key == KeyEvent.VK_UP && (!currentSnakeDirection.equals(Direction.DOWN))){
                currentSnakeDirection = Direction.UP;
            }

            if(key == KeyEvent.VK_DOWN && (!currentSnakeDirection.equals(Direction.UP))){
                currentSnakeDirection = Direction.DOWN;
            }

            if(key == KeyEvent.VK_N) {
                startGame();
            }

            if(key == KeyEvent.VK_P) {
                pauseGame();
            }
        }
    }

    /*
    private final Point[] wall = {
            new Point(1, 1),
            new Point(2, 1),
            new Point (3, 1),
            new Point(1, 2),
            new Point(1, 3),
    };
    */

    // Constructors

    public GamePanel() {
        addKeyListener(new SnakeKeyAdapter());
        setFocusable(true);
        setBackground(Color.black);
        setSize(getGameFieldSize());
        setPreferredSize(getGameFieldSize());
        //placeWall();
        // Необходимо переписать отрисовку счёта через Graphics
        scoreLabel.setForeground(Color.white);
        setGameFieldTheme(Theme.LIGHT);
        add(scoreLabel);
    }

    // Getters

    public int getGameFieldSideSize() {
        return cellSize * numberOfCells;
    }

    public Dimension getGameFieldSize() {
        return new Dimension(cellSize * numberOfCells, cellSize * numberOfCells);
    }

    // Setters

    public void setNumberOfCells(int numberOfCells) {
        this.numberOfCells = numberOfCells;
        setSize(getGameFieldSize());
        setPreferredSize(getGameFieldSize());
    }

    public void setGameSpeed(int gameSpeed) {
        this.gameSpeed = gameSpeed;
        timer.setDelay(gameSpeed);
    }

    public void setGameFieldTheme(GamePanel.Theme theme) {
        if (theme == Theme.DARK) {
            setBackground(Color.black);
            appleColor = Color.green;
            snakeHeadColor = Color.red;
            snakeBodyColor = Color.yellow;
            textColor = Color.white;
        }
        if (theme == Theme.LIGHT) {
            Color customBlack = new Color(43, 51, 26);
            setBackground(new Color(169, 203, 102));
            appleColor = Color.yellow;
            snakeHeadColor = Color.red;
            snakeBodyColor = customBlack;
            textColor = customBlack;
        }
    }

    // Draw methods

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isFirstGame) {
            drawMessage(g, "Press N to start new game!");
        } else {
            if (isGamePaused) {
                drawMessage(g, "Game paused");
            }
            if (!inGame) {
                drawMessage(g, "Game over!");
            }

            //drawWalls(g);
            drawApple(g);
            drawSnake(g);
        }
    }

    /*
    private void drawWalls(Graphics g) {
        g.setColor(Color.gray);
        for (Point point : wall) {
            g.fillRect(point.x, point.y, cellSize, cellSize);
        }
    }
    */

    private void drawApple(Graphics g) {
        g.setColor(appleColor);
        g.fillOval(applePosition.x, applePosition.y, cellSize, cellSize);
    }

    private void drawSnake(Graphics g) {
        for (int i = 0; i < snake.length; i++) {
            if (snake[i] != null) {
                if (i == 0) {
                    g.setColor(snakeHeadColor);
                    g.fillOval(snake[i].x, snake[i].y, cellSize, cellSize);
                    g.setColor(snakeBodyColor);
                } else {
                    g.fillOval(snake[i].x, snake[i].y, cellSize, cellSize);
                }
            }
        }
    }

    private void drawMessage(Graphics g, String message) {
        Font font = new Font("SAN_SERIF", Font.BOLD, 30);
        FontMetrics fontMetrics = getFontMetrics(font);

        g.setColor(textColor);
        g.setFont(font);
        g.drawString(message, (getGameFieldSideSize() - fontMetrics.stringWidth(message)) / 2, getGameFieldSideSize() / 2);
    }

    // Logic methods

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (inGame) {
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
        repaint();
    }

    public void startGame() {
        scoreCounter = 0;
        isFirstGame = false;
        inGame = true;
        isNeedToAskSaveScore = true;
        snake = new Point[numberOfCells * numberOfCells];
        locateApplePosition();
        locateSnakeInitialPosition();
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
    }

    public void pauseGame() {
        if (inGame) {
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
        scoreLabel.setText("Your score: " + scoreCounter);
    }

    private void moveSnake() {
        // Determines snake head new position, depending on current snake movement direction.
        Point snakeHeadNewPoint = new Point();
        switch (currentSnakeDirection) {
            case RIGHT:
                snakeHeadNewPoint.x = checkSnakePointCoordinateToBeInBounds(snake[0].x + cellSize);
                snakeHeadNewPoint.y = snake[0].y;
                break;
            case LEFT:
                snakeHeadNewPoint.x = checkSnakePointCoordinateToBeInBounds(snake[0].x - cellSize);
                snakeHeadNewPoint.y = snake[0].y;
                break;
            case UP:
                snakeHeadNewPoint.x = snake[0].x;
                snakeHeadNewPoint.y = checkSnakePointCoordinateToBeInBounds(snake[0].y - cellSize);
                break;
            case DOWN:
                snakeHeadNewPoint.x = snake[0].x;
                snakeHeadNewPoint.y = checkSnakePointCoordinateToBeInBounds(snake[0].y + cellSize);
                break;
        }

        // Moves all points of snake from position i to position i + 1, to the first position placed snake's head new point.
        // If there is no need to add new point to snake's body, point in last position removes.
        System.arraycopy(snake, 0, snake, 1, snake.length - 1);
        if (!isSnakeBodyNeededToBeIncreased) {
            for (int i = 0; i < snake.length; i++) {
                if (snake[i] != null && snake[i + 1] == null) {
                    snake[i] = null;
                }
            }
        } else {
            isSnakeBodyNeededToBeIncreased = false;
        }
        snake[0] = snakeHeadNewPoint;
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
        snake[0] = snakeInitialPositionPoint;

        int snakeInitialDirection = (int)(Math.random() * 4);
        switch (snakeInitialDirection) {
            case 0:
                currentSnakeDirection = Direction.UP;
                break;
            case 1:
                currentSnakeDirection = Direction.DOWN;
                break;
            case 2:
                currentSnakeDirection = Direction.LEFT;
                break;
            case 3:
                currentSnakeDirection = Direction.RIGHT;
                break;
        }

        for (int i = 1; i < snakeInitialSize; i++) {
            Point snakeBodyPoint = new Point();
            if (currentSnakeDirection.equals(Direction.UP)) {
                snakeBodyPoint.x = snake[0].x;
                snakeBodyPoint.y = checkSnakePointCoordinateToBeInBounds(snake[0].y + i * cellSize);
            } else if(currentSnakeDirection.equals(Direction.DOWN)) {
                snakeBodyPoint.x = snake[0].x;
                snakeBodyPoint.y = checkSnakePointCoordinateToBeInBounds(snake[0].y - i * cellSize);
            } else if(currentSnakeDirection.equals(Direction.LEFT)) {
                snakeBodyPoint.x = checkSnakePointCoordinateToBeInBounds(snake[0].x + i * cellSize);
                snakeBodyPoint.y = snake[0].y;
            } else {
                snakeBodyPoint.x = checkSnakePointCoordinateToBeInBounds(snake[0].x - i * cellSize);
                snakeBodyPoint.y = snake[0].y;
            }
            snake[i] = snakeBodyPoint;
        }
    }

    private Point locateRandomPositionPoint() {
        Point randomPositionPoint = new Point();
        randomPositionPoint.x = (int) (Math.random() * (numberOfCells)) * cellSize;
        randomPositionPoint.y = (int) (Math.random() * (numberOfCells)) * cellSize;
        return randomPositionPoint;
    }

    // Logic methods - Checking methods

    private void checkIsAppleEaten() {
        for (Point point : snake) {
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
        for (int i = 1; i < snake.length; i++) {
            if (snake[i] != null) {
                if (snake[0].x == snake[i].x && snake[0].y == snake[i].y) {
                    inGame = false;
                }
            }
        }
    }

    private int checkSnakePointCoordinateToBeInBounds(int pointCoordinate) {
        if (pointCoordinate == getGameFieldSideSize()) {
            return pointCoordinate - getGameFieldSideSize();
        } else if (pointCoordinate < 0) {
            return pointCoordinate + getGameFieldSideSize();
        }
        return pointCoordinate;
    }

    private void saveResult() {
        isNeedToAskSaveScore = false;
       int answer = JOptionPane.showConfirmDialog(
               this,
               "Want to save your score?",
               "Saving score",
               JOptionPane.YES_NO_OPTION);
       if (answer == JOptionPane.YES_OPTION) {
           try {
               String userName = JOptionPane.showInputDialog(this, "Enter your name", "Enter your name", JOptionPane.PLAIN_MESSAGE);
               if (userName.equals("")) {
                   userName = "Unnamed player";
               }
               String workingDirectory = System.getProperty("user.dir");
               File topScoreFile = new File(workingDirectory + "/top_score.dat");
               String userNameAndScore = userName + ":" + scoreCounter + "\n";
               if (!topScoreFile.exists()) {
                   topScoreFile.createNewFile();
               }
               Files.write(Paths.get(topScoreFile.toURI()), userNameAndScore.getBytes(), StandardOpenOption.APPEND);
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (IOException exception) {
               exception.printStackTrace();
           }
       }
    }
}
