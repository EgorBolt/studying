package Socks5Proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.print("Write port: ");
            String input = reader.readLine();
            String[] arguments = input.split(" ");
            if (arguments.length != 1) {
                System.err.println("ERROR: wrong amount of arguments, terminating the program");
                System.exit(-4);
            }

            short port = Short.parseShort(arguments[0]);
            Socks5Proxy proxy = new Socks5Proxy(port);
            proxy.doJob();
        }
    }
}