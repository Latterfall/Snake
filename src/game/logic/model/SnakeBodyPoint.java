package game.logic.model;

import java.awt.*;

public class SnakeBodyPoint extends Point {
    private Direction currentPointDirection;

    public SnakeBodyPoint() {}

    public SnakeBodyPoint(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public Direction getCurrentPointDirection() {
        return currentPointDirection;
    }

    public void setCurrentPointDirection(Direction currentPointDirection) {
        this.currentPointDirection = currentPointDirection;
    }
}
