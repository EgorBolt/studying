package ChatServerWS.Handlers;

import ChatServerWS.ServerInfo;
import ChatServerWS.Utils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xnio.streams.ChannelOutputStream;

public class GetUserHandler implements HttpHandler {
    private ServerInfo serverInfo;

    public GetUserHandler(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public void handleRequest(final HttpServerExchange he) throws Exception {
        String requestMethod = he.getRequestMethod().toString();
        String requestURI = he.getRequestURI();

        if ("GET".equals(requestMethod)) {
            if (requestURI.length() <= 7) {
                String header = he.getRequestHeaders().getFirst("Authorization");
                String[] headerSplit = header.split(" ");
                if (headerSplit.length == 1) {
                    Utils.endExchangeWithErrorCode(he, 401);
                }

                String userToken = headerSplit[1];
                JSONObject user = serverInfo.getActiveUser(userToken);

                if (user != null) {
                    StringBuffer sBuffer = new StringBuffer("{\"users\": [");

                    JSONArray activeUsers = serverInfo.getActiveUsers();
                    String sResult = Utils.formJSON(activeUsers, sBuffer);

                    he.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");

                    he.setStatusCode(200);
                    he.setResponseContentLength(sResult.length());
                    ChannelOutputStream os = new ChannelOutputStream(he.getResponseChannel());
                    os.write(sResult.getBytes());
                    he.endExchange();
                } else {
                    Utils.endExchangeWithErrorCode(he, 403);
                }
            } else {
                String id = requestURI.substring(7);
                int idInt = Integer.parseInt(id);
                JSONObject user = serverInfo.getActiveUser(idInt);

                if (user != null) {
                    String sResult = user.toString();

                    he.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
                    he.setStatusCode(200);
                    he.setResponseContentLength(sResult.length());
                    ChannelOutputStream os = new ChannelOutputStream(he.getResponseChannel());
                    os.write(sResult.getBytes());
                    he.endExchange();
                } else {
                    Utils.endExchangeWithErrorCode(he,404);
                }
            }
        } else {
            Utils.endExchangeWithErrorCode(he,405);
        }
    }
}