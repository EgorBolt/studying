package ChatServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONObject;

public class Server {

    public static void main(String[] args) throws Exception {
        ServerInfo si = new ServerInfo();
        ExecutorService executeIt = Executors.newCachedThreadPool();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 5);
        server.createContext("/login", new loginHandler(si));
        server.createContext("/logout", new logoutHandler(si));
        server.createContext("/users", new getUsersHandler(si));
        server.createContext("/users/", new userInfoHandler(si));
        server.createContext("/messages", new messageHandler(si));
        server.createContext("/updatelogins", new updateLoginsHandler(si));
        server.setExecutor(executeIt); // creates a default executor
        server.start();
    }

    static class loginHandler implements HttpHandler {
        ServerInfo serverInfo;

        loginHandler(ServerInfo serverInfo) {
            this.serverInfo = serverInfo;
        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            String requestMethod = he.getRequestMethod();
            Headers requestHeaders = he.getRequestHeaders();
            InputStream requestBody = he.getRequestBody();

            String response;

            if ("POST".equals(requestMethod)) {
                String header = requestHeaders.getFirst("Content-Type");
                if (header.equals("application/json")) {
                    Scanner sc = new Scanner(requestBody);
                    StringBuffer s = new StringBuffer();
                    while (sc.hasNextLine()) {
                        String str = sc.nextLine();
                        if (str.charAt(0) == '\t') {
                            str = str.substring(1);
                        }
                        s.append(str);
                    }

                    String sResult = s.toString();
                    sResult = sResult.replace('"', '\"');
                    JSONObject jo = new JSONObject(sResult);
                    String username = jo.getString("username");
                    jo.put("id", serverInfo.getUsersAmount() + 1);
                    jo.put("online", "true");
                    jo.put("token", username);
                    boolean addedUser = serverInfo.putUser(jo);
                    boolean addedActiveUser = serverInfo.putActiveUser(jo);

                    if (!addedActiveUser) {
                        he.getResponseHeaders().add("WWW-Authenticate", "Token realm=\'Username is already in use\'");
                        he.sendResponseHeaders(401, 0);
                        he.close();
                        return;
                    }
                    if (!addedUser) {
                        jo = serverInfo.getActiveUser(username);
                    }



                    sResult = jo.toString();

                    he.getResponseHeaders().add("Content-Type", "application/json");

                    he.sendResponseHeaders(200, sResult.length());
                    OutputStream os = he.getResponseBody();
                    os.write(sResult.getBytes());
                    he.close();
                } else {
                    generateError(he, 400);
                }
            } else {
                generateError(he, 405);
            }
        }
    }

    static class updateLoginsHandler implements HttpHandler {
        ServerInfo serverInfo;

        updateLoginsHandler(ServerInfo serverInfo) {
            this.serverInfo = serverInfo;
        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            String requestMethod = he.getRequestMethod();

            if ("GET".equals(requestMethod)) {
                JSONArray loginsUpdate = serverInfo.getUsers();
                StringBuffer sBuffer = new StringBuffer("{\"users\": [");
                for (int i = 0; i < loginsUpdate.length(); i++) {
                    JSONObject obj = (JSONObject)loginsUpdate.get(i);
                    sBuffer.append(obj.toString());
                    if (i < loginsUpdate.length() - 1) {
                            sBuffer.append(',');
                        }
                }

                sBuffer.append("]}");
                String sResult = sBuffer.toString();
                he.sendResponseHeaders(200, sResult.length());
                OutputStream os = he.getResponseBody();
                os.write(sResult.getBytes());
                he.close();
            } else {
                generateError(he, 405);
            }
        }

    }

    static class logoutHandler implements HttpHandler {
        ServerInfo serverInfo;

        logoutHandler(ServerInfo serverInfo) {
            this.serverInfo = serverInfo;
        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            String requestMethod = he.getRequestMethod();
            Headers requestHeaders = he.getRequestHeaders();

            String response;

            if ("POST".equals(requestMethod)) {
                String header = requestHeaders.getFirst("Authorization");
                String[] headerSplit = header.split(" ");
                if (headerSplit.length == 1) {
                    generateError(he, 401);
                }
                String userToken = headerSplit[1];
                JSONObject user = serverInfo.getActiveUser(userToken);
                if (user != null) {
                    String token = header.substring(6);
                    boolean deletedUser = serverInfo.deleteActiveUser(token);

                    if (deletedUser) {
                        JSONObject jo = new JSONObject("{\"message\":\"bye!\"}");
                        String sResult = jo.toString();

                        he.getResponseHeaders().add("Content-Type", "application/json");

                        he.sendResponseHeaders(200, sResult.length());
                        OutputStream os = he.getResponseBody();
                        os.write(sResult.getBytes());
                        os.close();
                        he.close();
                    } else {
                        he.sendResponseHeaders(400, 0);
                        he.close();
                    }
                } else {
                    generateError(he, 403);
                }
            } else {
                generateError(he, 405);
            }
        }
    }

    static class getUsersHandler implements HttpHandler {
        ServerInfo serverInfo;

        getUsersHandler(ServerInfo serverInfo) {
            this.serverInfo = serverInfo;
        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            String requestMethod = he.getRequestMethod();
            Headers requestHeaders = he.getRequestHeaders();
            InputStream requestBody = he.getRequestBody();

            if ("GET".equals(requestMethod)) {
                String header = requestHeaders.getFirst("Authorization");
                String[] headerSplit = header.split(" ");
                if (headerSplit.length == 1) {
                    generateError(he, 401);
                }
                String userToken = headerSplit[1];
                JSONObject user = serverInfo.getActiveUser(userToken);
                if (user != null) {
                    StringBuffer sBuffer = new StringBuffer("{\"users\": [");
                    JSONArray activeUsers = serverInfo.getActiveUsers();
                    for (int i = 0; i < activeUsers.length(); i++) {
                        JSONObject obj = (JSONObject) activeUsers.get(i);
                        sBuffer.append(obj.toString());
                        if (i < activeUsers.length() - 1) {
                            sBuffer.append(',');
                        }
                    }

                    sBuffer.append("]}");
                    String sResult = sBuffer.toString();

                    he.getResponseHeaders().add("Content-Type", "application/json");

                    he.sendResponseHeaders(200, sResult.length());
                    OutputStream os = he.getResponseBody();
                    os.write(sResult.getBytes());
                    he.close();
                } else {
                    generateError(he, 403);
                }
            } else {
                generateError(he, 405);
            }
        }
    }

    static class userInfoHandler implements HttpHandler {
        ServerInfo serverInfo;

        userInfoHandler(ServerInfo serverInfo) {
            this.serverInfo = serverInfo;
        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            String requestMethod = he.getRequestMethod();
            Headers requestHeaders = he.getRequestHeaders();
            InputStream requestBody = he.getRequestBody();
            URI uri = he.getRequestURI();

            if ("GET".equals(requestMethod)) {
                String header = requestHeaders.getFirst("Authorization");
                String[] headerSplit = header.split(" ");
                if (headerSplit.length == 1) {
                    generateError(he, 401);
                }
                String userToken = headerSplit[1];
                JSONObject userActive = serverInfo.getActiveUser(userToken);
                if (userActive != null) {
                    String id = uri.toString().substring(7);
                    int idInt = Integer.parseInt(id);
                    JSONObject user = serverInfo.getActiveUser(idInt);

                    if (user != null) {
                        String sResult = user.toString();

                        he.getResponseHeaders().add("Content-Type", "application/json");

                        he.sendResponseHeaders(200, sResult.length());
                        OutputStream os = he.getResponseBody();
                        os.write(sResult.getBytes());
                        he.close();
                    } else {
                        he.sendResponseHeaders(404, 0);
                        he.close();
                    }
                } else {
                    generateError(he, 403);
                }
            } else {
                generateError(he, 405);
            }
        }
    }

    static class messageHandler implements HttpHandler {
        ServerInfo serverInfo;

        messageHandler(ServerInfo serverInfo) {
            this.serverInfo = serverInfo;
        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            String requestMethod = he.getRequestMethod();
            Headers requestHeaders = he.getRequestHeaders();
            InputStream requestBody = he.getRequestBody();

            if ("POST".equals(requestMethod)) {
                List<String> headerAuth = requestHeaders.get("Authorization");
                String hAuth = headerAuth.get(0);
                List<String> headerCT = requestHeaders.get("Content-Type");
                String hCT = headerCT.get(0);
                String[] headerSplit = hAuth.split(" ");
                if (headerSplit.length == 1) {
                    generateError(he, 401);
                }
                String userCheck = headerSplit[1];
                JSONObject user = serverInfo.getActiveUser(userCheck);
                if (user != null && hCT.contains("application/json")) {
                    Scanner sc = new Scanner(requestBody);
                    StringBuffer s = new StringBuffer();
                    while (sc.hasNextLine()) {
                        String str = sc.nextLine();
                        if (str.charAt(0) == '\t') {
                            str = str.substring(1);
                        }
                        s.append(str);
                    }

                    String userToken = hAuth.substring(6);
                    String sResult = s.toString();
                    sResult = sResult.replace('"', '\"');
                    JSONObject jo = new JSONObject(sResult);
                    String msg = jo.getString("message");
                    jo.put("id", serverInfo.getMessagesAmount() + 1);
                    jo.put("message", msg);
                    jo.put("author", userToken);

                    serverInfo.putMessage(jo);

                    sResult = jo.toString();

                    he.getResponseHeaders().add("Content-Type", "application/json");

                    he.sendResponseHeaders(200, sResult.length());
                    OutputStream os = he.getResponseBody();
                    os.write(sResult.getBytes());
                    he.close();
                } else {
                    generateError(he, 403);
                }
            } else if ("GET".equals(requestMethod)) {
                String header = requestHeaders.getFirst("Authorization");
                String[] headerSplit = header.split(" ");
                if (headerSplit.length == 1) {
                    generateError(he, 401);
                }
                String userToken = headerSplit[1];
                JSONObject user = serverInfo.getActiveUser(userToken);
                if (user != null) {
                    URI uri = he.getRequestURI();
                    String s = uri.toString().substring(10);
                    String[] arguments = s.split("&");
                    if (arguments.length != 2) {
                        he.sendResponseHeaders(400, 0);
                        he.close();
                        return;
                    } else {
                        String[] parseOffset = arguments[0].split("=");
                        if (!(parseOffset[0].equals("offset"))) {
                            he.sendResponseHeaders(400, 0);
                            he.close();
                            return;
                        }
                        String[] parseCount = arguments[1].split("=");
                        if (!(parseCount[0].equals("count"))) {
                            he.sendResponseHeaders(400, 0);
                            he.close();
                            return;
                        }
                        int offset = Integer.parseInt(parseOffset[1]);
                        int count = Integer.parseInt(parseCount[1]);
                        StringBuffer sBuffer = new StringBuffer("{\n" + "\"messages\": [");
                        JSONArray msgs = serverInfo.printMessages(offset, count);
                        for (int i = 0; i < msgs.length(); i++) {
                            JSONObject obj = (JSONObject) msgs.get(i);
                            sBuffer.append(obj.toString());
                            if (i < msgs.length() - 1) {
                                sBuffer.append(',');
                            }
                        }

                        sBuffer.append("]}");
                        String sResult = sBuffer.toString();

                        he.getResponseHeaders().add("Content-Type", "application/json");

                        he.sendResponseHeaders(200, sResult.length());
                        OutputStream os = he.getResponseBody();
                        os.write(sResult.getBytes());
                        he.close();
                    }
                } else {
                    generateError(he, 400);
                }
            } else {
                generateError(he, 405);
            }
        }
    }

    private static void generateError(HttpExchange he, int errCode) {
        try {
            he.sendResponseHeaders(errCode, 0);
            he.close();
        } catch (IOException eIO) {
            eIO.printStackTrace();
        }
    }
}
