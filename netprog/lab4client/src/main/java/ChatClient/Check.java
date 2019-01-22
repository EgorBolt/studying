package ChatClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Check implements Runnable {
    private String userToken;

    Check(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public void run() {
        HashMap<String, String> usersStatuses = new HashMap<>();

        try {
            while (true) {
                //Thread.sleep(1000);
                String request = "http://localhost:8080/updatelogins";
                URL url = new URL(request);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                int code = connection.getResponseCode();
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
                        for (int i = 0; i < usersActive.length(); i++) {
                            JSONObject user = (JSONObject) usersActive.get(i);
                            String uUsername = user.getString("username");
                            if (!usersStatuses.containsKey(uUsername)) {
                                usersStatuses.put(uUsername, "");
                            }
                            if (!uUsername.equals(userToken)) {
                                String status = user.getString("online");
                                String value = usersStatuses.get(uUsername);
                                if (status.equals("false") && !value.equals(status)) {
                                    usersStatuses.put(uUsername, "false");
                                    System.out.println("User " + uUsername + " left the chat!\n");
                                } else if (status.equals("true") && !value.equals(status)) {
                                    usersStatuses.put(uUsername, "true");
                                    System.out.println("User " + uUsername + " joined the chat!\n");
                                } else if (status.equals("null") && !value.equals(status)) {
                                    usersStatuses.put(uUsername, "null");
                                    System.out.println("User " + uUsername + " dropped the chat!\n");
                                }
                            }
                        }
                    }

                } else {
                    System.out.println("Error in loginsupdate");
                }
            }

        }
        catch (ConnectException eConnect) {
            System.err.println("SERVER HAS BEEN SHUTTED DOWN RUN YOU FOOLS!");
            System.exit(-1);
        }
        catch (IOException eIO) {
            eIO.printStackTrace();
        }
    }
}
