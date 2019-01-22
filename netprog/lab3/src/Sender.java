import java.io.IOException;
import java.net.*;
import java.util.*;

public class Sender implements Runnable {
    private String nodeName;
    private int nodePort;
    private InetAddress parentIP;
    private int parentPort;
    private boolean parentInitialized;
    private Vector<byte[]> messageQueue;
    private Vector<Child> childrenList;
    private Vector<String> uuidList;

    Sender(String nodeName, Vector<byte[]> messageQueue, Vector<Child> childrenList, Vector<String> uuidList) {
        this.nodeName = nodeName;
        this.messageQueue = messageQueue;
        this.childrenList = childrenList;
        this.parentInitialized = false;
        this.uuidList = uuidList;
    }

    Sender(String nodeName, Vector<byte[]> messageQueue, Vector<Child> childrenList,
           Vector<String> uuidList, int nodePort, InetAddress parentIP, int parentPort) {
        this.nodeName = nodeName;
        this.parentPort = parentPort;
        this.nodePort = nodePort;
        this.messageQueue = messageQueue;
        this.childrenList = childrenList;
        this.parentIP = parentIP;
        this.parentInitialized = true;
        this.uuidList = uuidList;
    }

    public void run() {
        HashMap<String, String> messageHistory = new HashMap<>();
        HashMap<String, Integer> messageTry = new HashMap<>();
        int messageHistorySize = 20;

        if (parentInitialized) {
            setParentInitialized(false);
            try (DatagramSocket socket = new DatagramSocket()) {
                byte msg[] = generateMessage("C", "");
                DatagramPacket packet = new DatagramPacket(msg, msg.length, parentIP, parentPort);
                socket.send(packet);
            } catch (IOException eIO) {
                System.err.println("Warning: parent with such IP doesn't exist.");
            }
        }

        while (true) {

            if (messageQueue.size() > 0) {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    byte[] msgFromQueue = messageQueue.firstElement();
                    byte[] msgToSend;

                    if (msgFromQueue[0] == 'A') {
                        setParentInitialized(true);
                        System.out.println("Connection with parent " + parentIP + " has been established.");
                        messageQueue.remove(messageQueue.size() - 1);
                        continue;
                    }

                    if (msgFromQueue[0] == 'M') {
                        msgToSend = msgFromQueue;
                    }
                    else {
                        byte[] msgFromQueueWithoutInput;
                        msgFromQueueWithoutInput = Arrays.copyOfRange(msgFromQueue, 2, msgFromQueue.length);

                        UUID messageUUID = UUID.randomUUID();
                        uuidList.add(messageUUID.toString());
                        if (messageHistory.size() < messageHistorySize) {
                            messageHistory.put(messageUUID.toString(), new String(msgFromQueueWithoutInput, "UTF-8"));
                            messageTry.put(messageUUID.toString(), 5);
                        }

                        byte[] buf = generateMessage("M", messageUUID.toString() + ':');
                        msgToSend = new byte[msgFromQueue.length + buf.length - 2];
                        System.arraycopy(buf, 0, msgToSend, 0, buf.length);
                        System.arraycopy(msgFromQueueWithoutInput, 0, msgToSend, buf.length, msgFromQueueWithoutInput.length);

                    }

                    if (parentInitialized) {
                        DatagramPacket packet = new DatagramPacket(msgToSend, msgToSend.length, parentIP, parentPort);
                        try {
                            socket.send(packet);
                        } catch (NullPointerException eNullPointer) {
                            System.err.println("Warning: cant sent message.");
                            continue;
                        }
                    }
                    for (int i = 0; i < childrenList.size(); i++) {
                        DatagramPacket packet = new DatagramPacket(msgToSend, msgToSend.length, childrenList.get(i).getIpAddress(), childrenList.get(i).getPort());
                        socket.send(packet);
                    }
                    messageQueue.remove(messageQueue.size() - 1);

//                    HashMap<String, String> newMessageHistory = new HashMap<>();
//                    HashMap<String, Integer> newMessageTry = new HashMap<>();
//                    for (int i = 0; i < uuidList.size(); i++) {
//                        String key = uuidList.get(i);
//
//                        if (messageTry.containsKey(key)) {
//                            int value = messageTry.get(key) - 1;
//                            if (value > 0) {
//                                newMessageTry.put(key, value);
//                            }
//                        }
//                        if (messageHistory.containsKey(key) && newMessageTry.containsKey(key)) {
//                            String m = messageHistory.get(key);
//                            newMessageHistory.put(key, m);
//                        }
//
//                    }
//
//                    messageHistory = newMessageHistory;
//                    messageTry = newMessageTry;
//
//                    for (int i = 0; i < uuidList.size(); i++) {
//                        String key = uuidList.get(i);
//                        String m = messageHistory.get(key);
//                        byte[] mbyte = new byte[m.length()];
//                        m.getBytes(0, m.length(), mbyte, 0);
//                        messageQueue.add(mbyte);
//                    }

                } catch (IOException eIO) {
                    System.err.println(eIO.getMessage());
                }
            }
            else {
                continue;
            }
        }
    }

    private void setParentInitialized(boolean value) {
        this.parentInitialized = value;
    }

    private byte[] generateMessage(String messageType, String info)  {
        byte[] msg;
        String message = null;

        if (messageType.equals("C")) {
            message = messageType + ':' + nodeName + ':' + nodePort;
        }
        else if (messageType.equals("M")) {
            message = messageType + ':' + nodeName + ':' + info;
        }

        msg = new byte[message.length()];
        message.getBytes(0, message.length(), msg, 0);

        return msg;
    }
}
