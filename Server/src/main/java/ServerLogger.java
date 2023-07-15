import java.io.FileOutputStream;
import java.io.IOException;

public class ServerLogger {

    private static ServerLogger log = null;
    private int numberMsg = 0;

    private ServerLogger() {
    }

    public static ServerLogger getLog() {
        if (log == null) {
            log = new ServerLogger();
        }
        return log;
    }

    public void log(String msg) {
        numberMsg++;
        String str = "log № " + numberMsg + " " + msg + "\n";
        try (FileOutputStream fos = new FileOutputStream("server/logServer.txt", true)) {
            byte[] bytes = str.getBytes();
            fos.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}