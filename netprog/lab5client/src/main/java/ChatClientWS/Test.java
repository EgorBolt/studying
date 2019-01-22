package ChatClientWS;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;

@ClientEndpoint
public class Test {
    private String previousGUID = "";
    private String msg = "";
    private String author = "";
    private String guid = "";

    @OnOpen
    public void onOpen(Session session) {
        try {
            session.getBasicRemote().sendText("init");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @OnMessage
    public void processMessage(String message) {

        JSONObject obj = new JSONObject(message);
        JSONArray usersActive = obj.getJSONArray("messages");
        for (int i = 0; i < usersActive.length(); i++) {
            JSONObject user = (JSONObject) usersActive.get(i);
            author = user.getString("author");
            msg = user.getString("message");
            guid = user.getString("guid");
        }
        if (!previousGUID.equals(guid)) {
            previousGUID = guid;
            System.out.println(author + "> " + msg);
        }
    }

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }
}