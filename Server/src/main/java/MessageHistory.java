import java.util.LinkedList;

public class MessageHistory {

    private final int SIZE = 10;

    private final LinkedList<String> msgHistoryList;

    MessageHistory() {
        msgHistoryList = new LinkedList<>();
    }

    public LinkedList<String> getMsgHistoryList() {
        return msgHistoryList;
    }

    public void addMsg(String str) {
        if (msgHistoryList.size() < SIZE) {
            msgHistoryList.addFirst(str);
        } else {
            msgHistoryList.pollLast();
            msgHistoryList.addFirst(str);
        }
    }

    public String outputList() {
        if (msgHistoryList.size() == 0) {
            return "";
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (String s : msgHistoryList) {
            stringBuffer.append(s).append("\n");
        }
        return stringBuffer.toString();
    }
}