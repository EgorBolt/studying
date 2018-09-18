package ru.nsu.fit.g16201.Boldyrev;

import java.io.IOException;
import java.net.*;

public class Send extends Thread {
    private InetAddress groupMulticastIP;
    private int groupPort;


    public Send(String ipAddress, int port) {
        groupPort = port;
        try {
            groupMulticastIP = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException eUnknownHost) {
            eUnknownHost.printStackTrace();
        }
    }

    public void run() {
        try {
            byte[] message;
            String msg = "Hi!";
            message = msg.getBytes();

            while (true) {
                DatagramSocket sendSocket = new DatagramSocket();

                DatagramPacket packet = new DatagramPacket(message, message.length,
                        groupMulticastIP, groupPort);
                sendSocket.send(packet);
                sendSocket.close();
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException eInterrupted) {
                    eInterrupted.printStackTrace();
                }
                //System.out.println("Message has been sent");
            }
        } catch (SocketException eSocket) {
            eSocket.printStackTrace();
        } catch (IOException eIO) {
            eIO.printStackTrace();
        }
    }
}
