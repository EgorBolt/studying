package ChatClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Scanner;

public class ChatClient implements Runnable {
    private String userToken;

    ChatClient(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(System.in)) {
            String inputMsg = null;
            String username;
            int code;
            URL url;
            String token;
            HttpURLConnection connection;
            String request;

            username = getUserToken();

            inputMsg = in.nextLine();

            while (!inputMsg.equals("/logout")) {
                String[] msgParts = inputMsg.split(" ");
                switch (msgParts[0]) {
                    case "/login":
                        request = "http://localhost:8080/login";
                        url = new URL(request);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json");
                        try (DataOutputStream input = new DataOutputStream(connection.getOutputStream())) {
                            String params = "{" + "\"username\": \"" + username + "\"}";
                            input.write(params.getBytes());
                        }

                        code = connection.getResponseCode();
                        if (code == HttpURLConnection.HTTP_OK) {
                            try (InputStream out = connection.getInputStream()) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(out));
                                StringBuilder r = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    r.append(line);
                                }
                                JSONObject obj = new JSONObject(r.toString());
                                int id = obj.getInt("id");
                                token = obj.getString("token");
                                System.out.println("Your nickname in the chat: " + username);
                                System.out.println("Your ID in the chat: " + id);
                                System.out.println("Your token: " + token);
                                System.out.println("From now on, use your token to communicate with server.");
                            }

                        } else if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            String header = connection.getHeaderField("WWW-Authenticate");
                            System.out.println(header);
                        }
                        break;

                    case "/users":
                        token = msgParts[1];
                        request = "http://localhost:8080/users";
                        url = new URL(request);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Authorization", "Token " + token);

                        code = connection.getResponseCode();
                        if (code == HttpURLConnection.HTTP_OK) {
                            try (InputStream out = connection.getInputStream()) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(out));
                                StringBuilder r = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    r.append(line);
                                }
                                String s = r.toString();
                                JSONObject obj = new JSONObject(s);
                                JSONArray usersActive = obj.getJSONArray("users");
                                System.out.print("Active users to this moment: ");
                                for (int i = 0; i < usersActive.length(); i++) {
                                    JSONObject user = (JSONObject)usersActive.get(i);
                                    System.out.print(user.getString("username") + ' ');
                                }
                                System.out.println();
                            }

                        } else if (code == HttpURLConnection.HTTP_FORBIDDEN) {
                            System.out.println("Error 403 in /users");
                        } else if (code == HttpURLConnection.HTTP_BAD_METHOD) {
                            System.out.println("Error 405 in /users");
                        }
                        break;

                    case "/messages":


                    default:
                        token = username;
                        request = "http://localhost:8080/messages";
                        url = new URL(request);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Authorization", "Token " + token);
                        connection.setRequestProperty("Content-Type", "application/json");

                        try (DataOutputStream input = new DataOutputStream(connection.getOutputStream())) {
                            String params = "{" + "\"message\": \"" + inputMsg + "\"}";
                            input.write(params.getBytes());
                        }

                        code = connection.getResponseCode();
                        if (code == HttpURLConnection.HTTP_FORBIDDEN) {
                            System.out.println("Error 403 in /messages");
                        } else if (code == HttpURLConnection.HTTP_BAD_METHOD) {
                            System.out.println("Error 405 in /messages");
                        }
                        break;
                }

                inputMsg = in.nextLine();
            }
            request = "http://localhost:8080/logout";
            url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Token " + username);

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                String params = "";
                wr.write(params.getBytes());
            }


            try (InputStream out = connection.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(out));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    System.out.println(result.toString());
                }
            }
            System.exit(0);
        }
        catch (ConnectException eConnect) {
            System.err.println("SERVER HAS BEEN SHUTTED DOWN RUN YOU FOOLS!");
            System.exit(-1);
        }
        catch (IOException eIO) {
            eIO.printStackTrace();
        }

    }

    String getUserToken() {
        return userToken;
    }
}
