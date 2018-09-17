import ru.nsu.fit.g16201.Boldyrev.Receive;
import ru.nsu.fit.g16201.Boldyrev.Send;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Receive receiver = new Receive();
        Send sender = new Send(args[0], Integer.parseInt(args[1]));
        Scanner in = new Scanner(System.in);
        String message;

        receiver.start();
        sender.start();

    }
}
