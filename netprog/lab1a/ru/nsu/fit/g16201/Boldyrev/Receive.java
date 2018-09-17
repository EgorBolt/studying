package ru.nsu.fit.g16201.Boldyrev;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Receive extends Thread {
    private MulticastSocket multicastSocket;
    private InetAddress multicastGroup;
    private ArrayList<InetAddress> listAddress;

    public Receive(String ipAddress, int port) {
        try {
            multicastSocket = new MulticastSocket(port);
            multicastGroup = InetAddress.getByName(ipAddress);
            listAddress = new ArrayList<>();
        } catch (UnknownHostException eUnknownHost) {
            eUnknownHost.printStackTrace();
        } catch (IOException eIO) {
            eIO.printStackTrace();
        }
    }

    public void run() {
        try {
            byte[] message;
            String msg = "Hi!";
            message = msg.getBytes();
            multicastSocket.joinGroup(multicastGroup);


            while (true) {
                DatagramPacket packet = new DatagramPacket(message, message.length);
                multicastSocket.receive(packet);
                InetAddress address = packet.getAddress();
                if (!(listAddress.contains(address))) {
                    listAddress.add(address);
                }
                System.out.println(packet.getAddress());
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
            }
            //multicastSocket.close();
        } catch (IOException eIO) {
            eIO.printStackTrace();
        }
    }
}
