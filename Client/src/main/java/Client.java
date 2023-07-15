import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {

    private final Scanner scanner = new Scanner(System.in);
    private final ClientLogger logger = ClientLogger.getLog();
    private final SocketChannel socketChannel;
    private final int bufferSize;
    private final String clientExit;
    private final String name;

    public Client() throws IOException {
        ClientSettings settings = new ClientSettings();
        String host = settings.getHost();
        int port = settings.getPort();
        bufferSize = settings.getBufferSize();
        clientExit = settings.getClientExit();

        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        socketChannel = SocketChannel.open();
        socketChannel.connect(socketAddress);
        logger.log("Клиент запущен");

        name = nameRegistration();
        new Thread(this::writeMsg).start();
        new Thread(this::readMsg).start();
    }

    public String nameRegistration() {
        System.out.println("Добро пожаловать в Чат!");
        System.out.print("Введите своё имя: ");
        String str = scanner.nextLine();
        logger.log("Пользователь " + str + " авторизовался в чате");
        return str;
    }

    protected String inputMsg() {
        return scanner.nextLine();
    }

    protected void writeMsg() {
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm:ss a");
        try {
            socketChannel.write(ByteBuffer.wrap(name.getBytes(StandardCharsets.UTF_8)));
            System.out.println(name + " введите сообщение или " + clientExit + ", чтобы закончить");

            String msg;
            while (true) {
                Thread.sleep(400);
                msg = inputMsg();
                logger.log("(" + formatForDateNow.format(date) + ") Пользователь " + name + ": " + msg);
                if (clientExit.equalsIgnoreCase(msg) || "/serverClose".equals(msg)) {
                    socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                    logger.log("Пользователь " + name + " вышел из чата");
                    System.out.println("Вы вышли из чата");
                    Thread.sleep(2000);
                    break;
                }
                socketChannel.write(ByteBuffer.wrap(("(" + formatForDateNow.format(date) + ") " + name + ": " + msg)
                        .getBytes(StandardCharsets.UTF_8)));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("ПОТОК НА ЗАПИСЬ ЗАКРЫТ");
        }
    }

    protected void readMsg() {
        ByteBuffer inputBuffer = ByteBuffer.allocate(bufferSize);
        try {
            while (true) {
                int bytesCount = socketChannel.read(inputBuffer);
                final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                if (clientExit.equalsIgnoreCase(msg)) break;
                logger.log(msg);
                System.out.println(msg);
                inputBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("ПОТОК НА ЧТЕНИЕ ЗАКРЫТ");
            try {
                logger.log("Клиент закрыт");
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}