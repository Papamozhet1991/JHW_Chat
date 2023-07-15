import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Properties;

public class ServerChatTest {

    private static Server server;
    private static ServerChat serverChat;
    private static ServerSocketChannel serverChannel;
    private static SocketChannel socketChannel;

    String host;
    int port;

    @BeforeEach
    public void init() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("settings.properties"));
        host = properties.getProperty("HOST_NAME");
        port = Integer.parseInt(properties.getProperty("PORT"));

        //serverChannel = ServerSocketChannel.open();
        //serverChannel.bind(new InetSocketAddress(host, port));
        //server = new Server();
        //serverChat = new ServerChat(socketChannel, server);
        System.out.println("Тесты запущенны");
    }

    @BeforeAll
    public static void started() {
        System.out.println("Старт теста");
    }

    @AfterEach
    public void finished() {
        System.out.println("Окончание теста");
    }

    @AfterAll
    public static void finishedAll() {
        System.out.println("Тесты завершенны");
    }

    @Test
    public void testRun() throws IOException {

        SomeClient someClient = new SomeClient();
        String msg = "Сообщение от Клиента";
        //serverChat.start();
        someClient.setMsg(msg);
        someClient.writeMsg();

        //MatcherAssert.assertThat(result, Matchers.equalTo(expected));
    }


}