import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SomeClient {

    private final SocketChannel socketChannel;

    private String msg;

    public SomeClient() throws IOException {
        ServerSettings settings = new ServerSettings();
        String host = settings.getHost();
        int port = settings.getPort();

        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        socketChannel = SocketChannel.open();
        socketChannel.connect(socketAddress);
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    protected void writeMsg() {
        try {
            socketChannel.write(ByteBuffer.wrap((msg).getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}