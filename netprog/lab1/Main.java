import ru.nsu.fit.g16201.Boldyrev.Receive;
import ru.nsu.fit.g16201.Boldyrev.Send;

public class Main {
    public static void main(String args[]) {
        Receive receiver = new Receive(args[0], Integer.parseInt(args[1]));
        Send sender = new Send(args[0], Integer.parseInt(args[1]));

        sender.start();
        receiver.start();
    }
}
