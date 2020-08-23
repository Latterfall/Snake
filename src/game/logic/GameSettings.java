package game.logic;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class GameSettings {
    private int cellSize;
    private int snakeInitialSize;
    private int numberOfCells;
    private int gameSpeed;

    public GameSettings() {
        try {
            Properties properties = new Properties();

            String path = Paths.get("").toAbsolutePath().toString();
            System.out.println(path);

            properties.load(new FileInputStream("src/game/logic/game.properties"));
            cellSize = Integer.parseInt(properties.getProperty("cellSize"));
            snakeInitialSize = Integer.parseInt(properties.getProperty("snakeInitialSize"));
            numberOfCells = Integer.parseInt(properties.getProperty("numberOfCells"));
            gameSpeed = Integer.parseInt(properties.getProperty("gameSpeed"));
        } catch (IOException exception) {
            System.out.println("Error occurred while trying load setting from game.properties!");
            exception.printStackTrace();
        }
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public int getSnakeInitialSize() {
        return snakeInitialSize;
    }

    public void setSnakeInitialSize(int snakeInitialSize) {
        this.snakeInitialSize = snakeInitialSize;
    }

    public int getNumberOfCells() {
        return numberOfCells;
    }

    public void setNumberOfCells(int numberOfCells) {
        this.numberOfCells = numberOfCells;
    }

    public int getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(int gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    // Getters

    public int getGameFieldSideSize() {
        return cellSize * numberOfCells;
    }

    public Dimension getGameFieldSize() {
        return new Dimension(cellSize * numberOfCells, cellSize * numberOfCells);
    }
}
