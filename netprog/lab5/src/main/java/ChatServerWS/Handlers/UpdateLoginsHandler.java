package ChatServerWS.Handlers;

import ChatServerWS.ServerInfo;
import ChatServerWS.Utils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xnio.streams.ChannelOutputStream;

public class UpdateLoginsHandler implements HttpHandler {
    private ServerInfo serverInfo;

    public UpdateLoginsHandler(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public void handleRequest(final HttpServerExchange he) throws Exception {
        String requestMethod = he.getRequestMethod().toString();

        if ("GET".equals(requestMethod)) {
            JSONArray loginsUpdate = serverInfo.getUsers();
            StringBuffer sBuffer = new StringBuffer("{\"users\": [");
            String sResult = Utils.formJSON(loginsUpdate, sBuffer);

            he.setStatusCode(200);
            he.setResponseContentLength(sResult.length());
            ChannelOutputStream os = new ChannelOutputStream(he.getResponseChannel());
            os.write(sResult.getBytes());
            he.endExchange();
        } else {
            Utils.endExchangeWithErrorCode(he,405);
        }
    }
}