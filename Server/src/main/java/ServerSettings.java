import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerSettings {

    private final String host;
    private final int port;
    private final int bufferSize;
    private final String clientExit;

    public ServerSettings() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("server/settings.properties"));
        host = properties.getProperty("HOST_NAME");
        port = Integer.parseInt(properties.getProperty("PORT"));
        bufferSize = Integer.parseInt(properties.getProperty("BUFFER_SIZE"));
        clientExit = properties.getProperty("EXIT");
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public String getClientExit() {
        return clientExit;
    }

    @Override
    public String toString() {
        return "ServerSettings{" +
                "host = '" + host + '\'' +
                ", port = " + port +
                ", bufferSize = " + bufferSize +
                "clientExit = '" + clientExit + '\'' +
                '}';
    }
}