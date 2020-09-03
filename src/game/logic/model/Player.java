package game.logic.model;

import java.io.Serializable;
import java.net.Socket;

public class Player implements Serializable {
    private Snake snake;
    private String username;
    private Socket socket;

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
