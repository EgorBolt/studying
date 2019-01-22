import java.util.Scanner;
import java.util.Vector;

public class Input implements Runnable {
    private Vector<byte[]> messageQueue;

    Input(Vector<byte[]> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void run() {
        try (Scanner in = new Scanner(System.in)) {
            String inputMsg;

            inputMsg = in.nextLine();
            while (!inputMsg.equals("!exit")) {
                byte[] message = new byte[inputMsg.length() + 2];
                message[0] = 'I';
                message[1] = ':';
                inputMsg.getBytes(0, inputMsg.length(), message, 2);
                messageQueue.add(message);
                inputMsg = in.nextLine();
            }
            if (inputMsg.equals("!exit")) {
                System.exit(0);
            }
        }
    }
}
