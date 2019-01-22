package ChatServerWS.Handlers;

import ChatServerWS.ServerInfo;
import ChatServerWS.Utils;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class UpdateMsgWebsocket implements WebSocketConnectionCallback {
    private ServerInfo serverInfo;

    public UpdateMsgWebsocket(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel webSocketChannel) {
        webSocketChannel
                .getReceiveSetter()
                .set(new Updater(serverInfo));

        webSocketChannel.resumeReceives();
    }
}

class Updater extends AbstractReceiveListener {
    private ServerInfo serverInfo;

    public Updater(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) throws IOException {
        int offset = -1;
        int count = -1;

        while (true) {
            JSONArray msgs = serverInfo.printMessages(offset, count);
            StringBuffer sBuffer = new StringBuffer("{\n" + "\"messages\": [");

            String sResult = Utils.formJSON(msgs, sBuffer);

            WebSockets.sendText(sResult, channel, null);
        }
    }
}

    // onError
    // onCloseMessage
    // ...
    //                WebSockets.sendText("Current state of i: " + i, channel, null);


