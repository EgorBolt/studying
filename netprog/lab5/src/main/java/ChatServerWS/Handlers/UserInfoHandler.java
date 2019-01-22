package ChatServerWS.Handlers;

import ChatServerWS.ServerInfo;
import ChatServerWS.Utils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.json.JSONObject;
import org.xnio.streams.ChannelOutputStream;

public class UserInfoHandler implements HttpHandler {
    private ServerInfo serverInfo;

    public UserInfoHandler(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public void handleRequest(final HttpServerExchange he) throws Exception {
        String requestMethod = he.getRequestMethod().toString();
        String header = he.getRequestHeaders().getFirst("Authorization");
        String uri = he.getRequestURI();

        if ("GET".equals(requestMethod)) {
            String[] headerSplit = header.split(" ");
            if (headerSplit.length == 1) {
                Utils.endExchangeWithErrorCode(he,401);
            }
            String userToken = headerSplit[1];
            JSONObject userActive = serverInfo.getActiveUser(userToken);

            if (userActive != null) {
//                String id = uri.substring(7);
                String params = he.getQueryString();
//                int idInt = Integer.parseInt(id);
                int idInt = 0;
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
            } else {
                Utils.endExchangeWithErrorCode(he,403);
            }

        } else {
            Utils.endExchangeWithErrorCode(he,405);
        }
    }
}