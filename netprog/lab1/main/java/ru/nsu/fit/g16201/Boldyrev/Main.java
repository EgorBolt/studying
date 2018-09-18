package ru.nsu.fit.g16201.Boldyrev;

import static java.lang.System.exit;

public class Main {
    public static void main(String args[]) {
        Receive receiver = new Receive(args[0], Integer.parseInt(args[1]));
        Send sender = new Send(args[0], Integer.parseInt(args[1]));

        sender.start();
        receiver.start();
    }
}
