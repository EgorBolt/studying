package ru.nsu.fit.g16201.Boldyrev;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Send {
    private DatagramSocket sendSocket;
    private InetAddress multicastGroup;
    private byte[] message;

    public void multicast(String multicastMessage) throws IOException {
        multicastGroup = InetAddress.getByName("235.0.0.0");
        sendSocket = new DatagramSocket();
        message = multicastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(message, message.length,
                multicastGroup, 5000);
        sendSocket.send(packet);
        sendSocket.close();
    }
}
