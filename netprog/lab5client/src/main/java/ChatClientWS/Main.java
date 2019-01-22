package ChatClientWS;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String username;
        Scanner in = new Scanner(System.in);
        System.out.print("Welcome to the HTTP chat! Please write your username: ");
        username = in.nextLine();
        Thread chatClient = new Thread(new ChatClient(username));
        Thread loginUpdater = new Thread(new Check(username));
        loginUpdater.start();
        chatClient.start();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException eIn) {
            eIn.printStackTrace();
        }
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String uri = "ws://localhost:8080/test";
            System.out.println("Connecting to " + uri);
            container.connectToServer(Test.class, URI.create(uri));
        } catch (DeploymentException | IOException ex) {
            ex.printStackTrace();
        }
    }
}
