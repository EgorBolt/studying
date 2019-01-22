package ChatClient;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String username;
        Scanner in = new Scanner(System.in);
        System.out.print("Welcome to the HTTP chat! Please write your username: ");
        username = in.nextLine();
        Thread chatClient = new Thread(new ChatClient(username));
        Thread loginUpdater = new Thread(new Check(username));
        Thread updateMsg = new Thread(new UpdateMsg(username));
        loginUpdater.start();
        chatClient.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException eIn) {
            eIn.printStackTrace();
        }
        updateMsg.start();
    }
}
