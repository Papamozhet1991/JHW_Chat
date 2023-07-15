import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ServerChat extends Thread {

    private final ServerLogger logger = ServerLogger.getLog();
    private final int bufferSize;
    private final String clientExit;

    private final SocketChannel socketChannel;
    private final Server server;

    public ServerChat(SocketChannel socketChannel, Server server) throws IOException {
        this.socketChannel = socketChannel;
        this.server = server;
        bufferSize = server.getBufferSize();
        clientExit = server.getClientExit();
        start();
    }

    @Override
    public void run() {
        final ByteBuffer inputBuffer = ByteBuffer.allocate(bufferSize);
        String name = "";
        try {
            if (server.getMessageHistory().getMsgHistoryList().size() > 0) {
                socketChannel.write(ByteBuffer.wrap(("SERVER: " +
                        server.getMessageHistory().getMsgHistoryList().size() + " последних сообщений:\n" +
                        server.getMessageHistory().outputList()).getBytes(StandardCharsets.UTF_8)));
            }
            name = getNameClient(inputBuffer);

            while (socketChannel.isConnected()) {
                int bytesCount = socketChannel.read(inputBuffer);
                if (bytesCount == -1) break;
                final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                inputBuffer.clear();

                if (clientExit.equalsIgnoreCase(msg) || "/serverClose".equals(msg)) {
                    socketChannel.write(ByteBuffer.wrap((clientExit).getBytes(StandardCharsets.UTF_8)));
                    String str = name + " покинул чат";
                    logger.log(str);
                    sendMsg(str);
                    inputBuffer.clear();
                    serverClose(name, msg);
                    break;
                }
                server.getMessageHistory().addMsg(msg);

                Thread.sleep(200);
                logger.log("SERVER: Получено сообщение от клиента: " + msg);
                System.out.println("SERVER: Получено сообщение от клиента: " + msg);
                sendMsg(msg);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this);
            logger.log("ВЫХОД КЛИЕНТА: " + name);
            System.out.println("ВЫХОД КЛИЕНТА: " + name);
        }
    }

    protected String getNameClient(ByteBuffer inputBuffer) throws IOException {
        int bytesCount = socketChannel.read(inputBuffer);
        String name = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
        inputBuffer.clear();

        String str = "В чат зашел: " + name;
        logger.log(str);
        System.out.println("SERVER: " + str);
        sendMsg(str);
        inputBuffer.clear();
        return name;
    }

    protected void sendMsg(String msg) {
        try {
            if (server.getClientList().size() == 1) {
                socketChannel.write(ByteBuffer.wrap(("В чате пока нет собеседников =(").getBytes(StandardCharsets.UTF_8)));
            }
            for (ServerChat s : server.getClientList()) {
                if (s.equals(this)) {
                    continue;
                }
                s.socketChannel.write(ByteBuffer.wrap((msg).getBytes(StandardCharsets.UTF_8)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void serverClose(String name, String msg) {
        if ("/admin".equals(name) && "/serverClose".equals(msg)) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.log("Администратором: " + name + " введена команда на остановку сервера");
            logger.log("Команда: " + msg);
            server.serverChannelClose();
        }
    }
}