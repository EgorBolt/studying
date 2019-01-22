package ChatServerWS.Handlers;

import ChatServerWS.ServerInfo;
import ChatServerWS.Utils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import org.json.JSONObject;
import org.xnio.streams.ChannelInputStream;
import org.xnio.streams.ChannelOutputStream;

import java.util.Scanner;
import java.util.UUID;

public class MessagesHandler implements HttpHandler {
    private ServerInfo serverInfo;

    public MessagesHandler(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public void handleRequest(final HttpServerExchange he) throws Exception {
        String requestMethod = he.getRequestMethod().toString();
        HeaderMap header = he.getRequestHeaders();

        if ("POST".equals(requestMethod)) {
            String hCT = header.getFirst("Content-Type");
            String hAuth = header.getFirst("Authorization");
            String[] headerSplit = hAuth.split(" ");
            if (headerSplit.length == 1) {
                Utils.endExchangeWithErrorCode(he,401);
            }
            String userCheck = headerSplit[1];
            JSONObject user = serverInfo.getActiveUser(userCheck);
            if (user != null && hCT.contains("application/json")) {
                ChannelInputStream requestBody = new ChannelInputStream(he.getRequestChannel());
                Scanner sc = new Scanner(requestBody);

                String sResult = Utils.readFromScanner(sc);

                String userToken = hAuth.substring(6);
                JSONObject jo = new JSONObject(sResult);
                String msg = jo.getString("message");
                String guid = UUID.randomUUID().toString();
                jo.put("id", serverInfo.getMessagesAmount() + 1);
                jo.put("message", msg);
                jo.put("author", userToken);
                jo.put("guid", guid);

                serverInfo.putMessage(jo);

                sResult = jo.toString();

                he.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
                he.setStatusCode(200);
                he.setResponseContentLength(sResult.length());
                ChannelOutputStream os = new ChannelOutputStream(he.getResponseChannel());
                os.write(sResult.getBytes());
                he.endExchange();

            } else {
                Utils.endExchangeWithErrorCode(he,403);
            }
        } else {
            Utils.endExchangeWithErrorCode(he,405);
        }
    }
}