import ru.nsu.fit.g16201.Boldyrev.Receive;
import ru.nsu.fit.g16201.Boldyrev.Send;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Receive receiver = new Receive();
        Send sender = new Send();
        Scanner in = new Scanner(System.in);
        String message;

        receiver.start();
        while (!(message = in.nextLine()).equals("end")) {
            try {
                sender.multicast(message);
            } catch (IOException eIO) {
                eIO.printStackTrace();
            }
        }
    }
}
