import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.print("Write lport, rhost, rport: ");
            String input = reader.readLine();
            String[] arguments = input.split(" ");
            if (arguments.length != 3) {
                System.err.println("ERROR: wrong amount of arguments, terminating the program");
                System.exit(-4);
            }

            int lport = Integer.parseInt(arguments[0]);
            String rhost = arguments[1];
            int rport = Integer.parseInt(arguments[2]);

            Forwarder forwarder = new Forwarder(lport, rhost, rport);
            System.out.println("Insert in browser: http://127.0.0.1:" + lport + "/gallery/");
            forwarder.doJob();
        }
    }
}
