package game.ui.panels;

import game.logic.GameLogicManager;
import game.logic.GameSettings;
import game.logic.LocalPlayerKeyAdapter;
import game.logic.model.Snake;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {
    //private final JLabel scoreLabel = new JLabel("Your score: " + scoreCounter);

    private GameLogicManager gameLogicManager;
    private GameSettings gameSettings;

    private Color appleColor;
    private Color snakeBodyColor;
    private Color textColor;

    public enum Theme {
        DARK, LIGHT
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

    public GamePanel(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
        setFocusable(true);
        setBackground(Color.black);
        setSize(gameSettings.getGameFieldSize());
        setPreferredSize(gameSettings.getGameFieldSize());
        //placeWall();
        // Необходимо переписать отрисовку счёта через Graphics
        //scoreLabel.setForeground(Color.white);
        setGameFieldTheme(Theme.LIGHT);
        //add(scoreLabel);
    }

    // Setters

    public void setNumberOfCells(int numberOfCells) {
        //this.numberOfCells = numberOfCells;
        setSize(gameSettings.getGameFieldSize());
        setPreferredSize(gameSettings.getGameFieldSize());
    }


    public void setGameFieldTheme(GamePanel.Theme theme) {
        if (theme == Theme.DARK) {
            setBackground(Color.black);
            appleColor = Color.green;
            snakeBodyColor = Color.yellow;
            textColor = Color.white;
        }
        if (theme == Theme.LIGHT) {
            Color customBlack = new Color(43, 51, 26);
            setBackground(new Color(169, 203, 102));
            appleColor = Color.yellow;
            snakeBodyColor = customBlack;
            textColor = customBlack;
        }
    }

    public void setGameLogicManager(GameLogicManager gameLogicManager) {
        this.gameLogicManager = gameLogicManager;
        addKeyListener(new LocalPlayerKeyAdapter(gameLogicManager));
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    // Draw methods

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameLogicManager.isFirstGame()) {
            drawMessage(g, "Press N to start new game!");
        } else {
            if (gameLogicManager.isGamePaused()) {
                drawMessage(g, "Game paused");
            }
            if (!gameLogicManager.isInGame()) {
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
        Point applePosition = gameLogicManager.getApplePosition();
        int cellSize = gameSettings.getCellSize();
        g.setColor(appleColor);
        g.fillOval(applePosition.x, applePosition.y, cellSize, cellSize);
    }

    private void drawSnake(Graphics g) {
        int cellSize = gameSettings.getCellSize();
        g.setColor(snakeBodyColor);
        gameLogicManager.getLocalSnake().snakeBodyPoints.forEach(point -> g.fillOval(point.x, point.y, cellSize, cellSize));

        List<Snake> otherPlayersSnakesList = gameLogicManager.getOtherPlayersSnakes();
        if (!otherPlayersSnakesList.isEmpty()) {
            otherPlayersSnakesList.forEach(snake -> {
                snake.snakeBodyPoints.forEach(point -> g.fillOval(point.x, point.y, cellSize, cellSize));
            });
        }
    }

    private void drawMessage(Graphics g, String message) {
        Font font = new Font("Courier New", Font.BOLD, 25);
        FontMetrics fontMetrics = getFontMetrics(font);

        g.setColor(textColor);
        g.setFont(font);
        g.drawString(message, (gameSettings.getGameFieldSideSize() - fontMetrics.stringWidth(message)) / 2, gameSettings.getGameFieldSideSize() / 2);
    }
}
