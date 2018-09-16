package ru.nsu.fit.g16201.Boldyrev;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receive extends Thread {
    protected MulticastSocket multicastSocket = null;
    protected byte[] message = new byte[256];

    public void run() {
        try {
            multicastSocket = new MulticastSocket(5000);
            InetAddress multicastGroup = InetAddress.getByName("235.0.0.0");
            multicastSocket.joinGroup(multicastGroup);
            while(true) {
                DatagramPacket packet = new DatagramPacket(message, message.length);
                multicastSocket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
                if ("end".equals(received)) {
                    break;
                }
            }
            multicastSocket.leaveGroup(multicastGroup);
            multicastSocket.close();
        } catch (IOException eIO) {
            eIO.printStackTrace();
        }
    }
}
