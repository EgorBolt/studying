package ChatServerWS.Handlers;

import ChatServerWS.ServerInfo;
import ChatServerWS.Utils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.json.JSONObject;
import org.xnio.streams.ChannelInputStream;
import org.xnio.streams.ChannelOutputStream;

import java.util.Scanner;

public class LoginHandler implements HttpHandler {
    private ServerInfo serverInfo;

    public LoginHandler(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public void handleRequest(final HttpServerExchange he) throws Exception {
        String requestMethod = he.getRequestMethod().toString();
        String header = he.getRequestHeaders().getFirst("Content-Type");


        if ("POST".equals(requestMethod)) {
            if (header.equals("application/json")) {
                ChannelInputStream requestBody = new ChannelInputStream(he.getRequestChannel());
                Scanner sc = new Scanner(requestBody);
                String sResult = Utils.readFromScanner(sc);

                JSONObject jo = new JSONObject(sResult);
                String username = jo.getString("username");
                jo.put("id", serverInfo.getUsersAmount() + 1);
                jo.put("online", "true");
                jo.put("token", username);
                boolean addedUser = serverInfo.putUser(jo);
                boolean addedActiveUser = serverInfo.putActiveUser(jo);

                if (!addedActiveUser) {
                    he.getResponseHeaders().add(Headers.WWW_AUTHENTICATE, "Token realm=\'Username is already in use\'");
                    Utils.endExchangeWithErrorCode(he,401);
                }
                if (!addedUser) {
                    jo = serverInfo.getActiveUser(username);
                }

                sResult = jo.toString();

                he.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
                he.setStatusCode(200);
                he.setResponseContentLength(sResult.length());
                ChannelOutputStream os = new ChannelOutputStream(he.getResponseChannel());
                os.write(sResult.getBytes());
                he.endExchange();
            } else {
                Utils.endExchangeWithErrorCode(he,400);
            }
        } else {
            Utils.endExchangeWithErrorCode(he,405);
        }
    }
}
