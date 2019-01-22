import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerFactory {
    private static ExecutorService executeIt = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Error: the amount of arguments is not one.");
            System.exit(-1);
        }

        try (
            ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
        ) {
            System.out.println("Server has been started.");

            while (!server.isClosed()) {
                Socket client = server.accept();
                executeIt.execute(new Server(client));
                System.out.println("Connection from IP " + client.getInetAddress() + " has been accepted.");
            }

        } catch (IOException eIO) {
            System.err.println(eIO.getMessage());
        } finally {
            executeIt.shutdown();
        }
    }
}
