import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

public class Receiver implements Runnable {
    private int nodePort;
    private int packagesLoss;
    private Vector<byte[]> messageQueue;
    private Vector<Child> childrenList;
    private Vector<String> uuidList;

    Receiver(int nodePort, int packagesLoss, Vector<byte[]> messageQueue, Vector<Child> childrenList, Vector<String> uuidList) {
        this.nodePort = nodePort;
        this.packagesLoss = packagesLoss;
        this.messageQueue = messageQueue;
        this.childrenList = childrenList;
        this.uuidList = uuidList;
    }

    public void run() {
        Random random = new Random();
        try {
            byte[] messageBuffer = new byte[10000];
            DatagramSocket socket = new DatagramSocket(nodePort);
            while (true) {
                DatagramPacket packet = new DatagramPacket(messageBuffer, messageBuffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                if (message.charAt(0) == 'C') {
                    String[] subStrings;
                    subStrings = message.split(":");
                    InetAddress childAddress = packet.getAddress();
                    Child newChild = new Child(subStrings[1], childAddress, Integer.parseInt(subStrings[2]));
                    childrenList.add(newChild);
                    byte[] responseByte = new byte[1];
                    responseByte[0] = 'A';
                    DatagramPacket response = new DatagramPacket(responseByte, 1, childAddress, Integer.parseInt(subStrings[2]));
                    DatagramSocket s = new DatagramSocket();
                    s.send(response);
                }
                else if (message.charAt(0) == 'A') {
                    byte[] response = new byte[1];
                    response[0] = 'A';
                    messageQueue.add(response);
                }
                else {
                    int decision = random.nextInt(1) + 100;
                    if (decision >= packagesLoss) {
                        String[] subStrings;
                        subStrings = message.split(":");
                        if (uuidList.contains(subStrings[2])) {
                            uuidList.remove(subStrings[2]);
                        }
                        else {
                            System.out.println(subStrings[1] + ": " + subStrings[3]);
                            byte[] msgToQueue = new byte[message.length()];
                            message.getBytes(0, message.length(), msgToQueue, 0);
                            messageQueue.add(msgToQueue);
                        }
                    }
                }
            }
        } catch (IOException eIO) {
            eIO.printStackTrace();
        }
    }
}