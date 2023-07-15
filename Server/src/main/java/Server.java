import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private final ServerLogger logger = ServerLogger.getLog();
    private final MessageHistory messageHistory;
    private final ServerSocketChannel serverChannel;
    private SocketChannel socketChannel;
    private final List<ServerChat> clientList;
    private final int bufferSize;
    private final String clientExit;

    public Server() throws IOException {
        ServerSettings settings = new ServerSettings();
        messageHistory = new MessageHistory();
        clientList = new ArrayList<>();

        String host = settings.getHost();
        int port = settings.getPort();
        bufferSize = settings.getBufferSize();
        clientExit = settings.getClientExit();

        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(host, port));
        logger.log("ЗАПУСК СЕРВЕРА!");
        System.out.println("ЗАПУСК СЕРВЕРА!");
        acceptClient();
    }

    protected void acceptClient() throws IOException {
        try {
            while (true) {
                socketChannel = serverChannel.accept();
                logger.log("Подключение нового клиента");
                clientList.add(new ServerChat(socketChannel, this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socketChannel.close();
        }
    }

    public void removeClient(ServerChat serverChat) {
        clientList.remove(serverChat);
    }

    public MessageHistory getMessageHistory() {
        return messageHistory;
    }

    public List<ServerChat> getClientList() {
        return clientList;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public String getClientExit() {
        return clientExit;
    }

    public void serverChannelClose() {
        try {
            serverChannel.close();
            socketChannel.close();
            logger.log("СЕРВЕР ОСТАНОВЛЕН");
            System.out.println("СЕРВЕР ОСТАНОВЛЕН");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}