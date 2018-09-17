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
    private ArrayList<IPInfo> listAddress;
    private int givenPort;

    public Receive(String ipAddress, int port) {
        try {
            givenPort = port;
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
            //listAddress = Collections.synchronizedList(listAddress);
            CheckConnection checkConnection = new CheckConnection();

            while (true) {
                DatagramPacket packet = new DatagramPacket(message, message.length);
                multicastSocket.receive(packet);
                InetAddress address = packet.getAddress();
                IPInfo gotAdd = new IPInfo(address, 3);
                if (!(listAddress.contains(gotAdd)) && listAddress.size() > 0) {
                    checkConnection.checkConnect(listAddress);
                    System.out.println("Adding new IP address:");
                    listAddress.add(gotAdd);
                    checkConnection.listAll(listAddress);
                }
                else {
                    checkConnection.checkConnect(listAddress);
                }
            }
            //multicastSocket.close();
        } catch (IOException eIO) {
            eIO.printStackTrace();
        }
    }

}
