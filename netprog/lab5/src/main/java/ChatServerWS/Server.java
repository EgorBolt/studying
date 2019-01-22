package ChatServerWS;

import ChatServerWS.Handlers.*;

import io.undertow.Handlers;
import io.undertow.Undertow;

public class Server {
    public static void main(String[] args) {
        ServerInfo si = new ServerInfo();
        int port = 8080;
        String host = "localhost";

        Undertow server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(
                        Handlers.path()
                            .addPrefixPath("/login", new LoginHandler(si))
                            .addPrefixPath("/logout", new LogoutHandler(si))
                            .addPrefixPath("/users", new GetUserHandler(si))
                            .addPath("/users/{id}", new UserInfoHandler(si))
                            .addPrefixPath("/messages", new MessagesHandler(si))
                            .addPrefixPath("/updatelogins", new UpdateLoginsHandler(si))
                            .addExactPath("/test", Handlers.websocket(new UpdateMsgWebsocket(si)))
            )
                .build();
        server.start();
    }
}



