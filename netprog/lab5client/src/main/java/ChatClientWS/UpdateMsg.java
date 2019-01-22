package ChatClientWS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateMsg implements Runnable {
    private String userToken;

    UpdateMsg(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public void run() {
        String previousMessage = "";
        String msg = "";
        String author = "";

        try {
            System.out.println("Message history: ");
            String request = "http://localhost:8080/messages?offset=0&count=105";
            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Token " + userToken);
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
                    JSONArray usersActive = obj.getJSONArray("messages");
                    for (int i = 0; i < usersActive.length(); i++) {
                        JSONObject user = (JSONObject) usersActive.get(i);
                        author = user.getString("author");
                        msg = user.getString("message");
                        if (!previousMessage.equals(msg)) {
                            System.out.println(author + "> " + msg);
                            previousMessage = msg;
                        }
                    }
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
