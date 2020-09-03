package game.logic.model;

import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;

public class Snake implements Serializable {
    public Direction currentSnakeDirection;
    public LinkedList<Point> snakeBodyPoints;

    public Snake() {
        snakeBodyPoints = new LinkedList<>();
    }
}
