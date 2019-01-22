package ChatServerWS.Handlers;

import ChatServerWS.ServerInfo;
import ChatServerWS.Utils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.json.JSONObject;
import org.xnio.streams.ChannelOutputStream;

public class LogoutHandler implements HttpHandler {
    private ServerInfo serverInfo;

    public LogoutHandler(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public void handleRequest(final HttpServerExchange he) throws Exception {
        String requestMethod = he.getRequestMethod().toString();

        if ("POST".equals(requestMethod)) {
            String header = he.getRequestHeaders().getFirst("Authorization");
            String[] headerSplit = header.split(" ");
            if (headerSplit.length == 1) {
                Utils.endExchangeWithErrorCode(he,401);
            }
            String userToken = headerSplit[1];
            JSONObject user = serverInfo.getActiveUser(userToken);
            if (user != null) {
                String token = header.substring(6);
                boolean deletedUser = serverInfo.deleteActiveUser(token);
                if (deletedUser) {
                    JSONObject jo = new JSONObject("{\"message\":\"bye!\"}");
                    String sResult = jo.toString();

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
                Utils.endExchangeWithErrorCode(he,403);
            }
        } else {
            Utils.endExchangeWithErrorCode(he,405);
        }
    }
}
