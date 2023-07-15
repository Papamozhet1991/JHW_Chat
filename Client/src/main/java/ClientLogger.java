import java.io.FileOutputStream;
import java.io.IOException;

public class ClientLogger {
    private static ClientLogger log = null;
    private int numberMsg = 0;

    private ClientLogger() {
    }

    public static ClientLogger getLog() {
        if (log == null) {
            log = new ClientLogger();
        }
        return log;
    }

    public void log(String msg) {
        numberMsg++;
        String str = "log № " + numberMsg + " " + msg + "\n";
        try (FileOutputStream fos = new FileOutputStream("client/logClient.txt", true)) {
            byte[] bytes = str.getBytes();
            fos.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}