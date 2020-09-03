package game.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiplayerConnectionManager implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(8546));
            while (true) {
                Socket clientSocket = serverSocket.accept();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

                printWriter.println("Howdy!");

                String message;
                while ((message  = bufferedReader.readLine()) != null) {
                    if (message.equals("exit")) {
                        printWriter.println("Bye!");
                        clientSocket.close();
                        return;
                    } else {
                        printWriter.println("Echo: " + message);
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
