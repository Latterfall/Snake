package game.logic;

import game.model.Direction;
import game.model.Snake;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static game.model.Direction.RIGHT;

public class LocalPlayerKeyAdapter extends KeyAdapter {
    private GameLogicManager gameLogicManager;

    public LocalPlayerKeyAdapter(GameLogicManager gameLogicManager) {
        this.gameLogicManager = gameLogicManager;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        Snake snake = gameLogicManager.getSnake();

        if (snake != null) {
            Direction currentSnakeDirection = snake.currentSnakeDirection;
            if (key == KeyEvent.VK_LEFT && (!currentSnakeDirection.equals(RIGHT))) {
                snake.currentSnakeDirection = Direction.LEFT;
            } else if (key == KeyEvent.VK_RIGHT && (!currentSnakeDirection.equals(Direction.LEFT))) {
                snake.currentSnakeDirection = RIGHT;
            } else if (key == KeyEvent.VK_UP && (!currentSnakeDirection.equals(Direction.DOWN))) {
                snake.currentSnakeDirection = Direction.UP;
            } else if (key == KeyEvent.VK_DOWN && (!currentSnakeDirection.equals(Direction.UP))) {
                snake.currentSnakeDirection = Direction.DOWN;
            }
        }

        if (key == KeyEvent.VK_N) {
            gameLogicManager.startGame();
        }

        if (key == KeyEvent.VK_P) {
            gameLogicManager.pauseGame();
        }
    }
}
