package game.logic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class MultiplayerConnectionManager implements Runnable {
    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8546));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while(true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectedKey = iterator.next();

                    if (selectedKey.isAcceptable()) {
                        SocketChannel userChannel = serverSocketChannel.accept();
                        userChannel.configureBlocking(false);
                        userChannel.register(selector, SelectionKey.OP_READ);
                    }

                    if (selectedKey.isReadable()) {
                        SocketChannel userChannel = (SocketChannel) selectedKey.channel();
                        userChannel.read(byteBuffer);
/*                        for (byte b : byteBuffer.array()) {
                            if ((char) b == '\n') {
                                System.out.println(new String(byteBuffer.array(), StandardCharsets.UTF_8));
                            }
                        }
                        byteBuffer.clear();*/
                        System.out.println(new String(byteBuffer.array(), StandardCharsets.UTF_8));
                    }

                    iterator.remove();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
