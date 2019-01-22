package ChatServer;

import org.json.JSONArray;
import org.json.JSONObject;

class ServerInfo {
    private JSONArray users;
    private JSONArray usersActive;
    private JSONArray messages;

    ServerInfo() {
        this.users = new JSONArray();
        this.usersActive = new JSONArray();
        this.messages = new JSONArray();
    }

    int getUsersAmount() {
        return users.length();
    }

    int getMessagesAmount() { return messages.length(); }

    void putMessage(JSONObject msg) {
        messages.put(msg);
    }

    JSONArray printMessages(int offset, int count) {
        JSONArray messages = getMessages();
        JSONArray result = new JSONArray();
        int begin = offset;
        int end = count;

        if (begin == -1 && end == -1 && messages.length() > 0) {
            begin = messages.length() - 1;
            end = messages.length();
        }
        else {
            if (begin > messages.length()) {
                begin = 0;
            }
            if (end > messages.length()) {
                if (messages.length() > 100) {
                    end = 100;
                } else {
                    end = messages.length();
                }
            }
        }

        for (int i = begin; i < end; i++) {
            JSONObject obj = (JSONObject)messages.get(i);
            result.put(obj);
        }

        return result;
    }

    boolean putUser(JSONObject candidate) {
        JSONArray users = getUsers();
        JSONObject obj;
        String userToken;
        String candidateToken;
        boolean hasUser = false;
        candidateToken = candidate.getString("token");

        for (int i = 0; i < users.length(); i++) {
            obj = (JSONObject)users.get(i);
            userToken = obj.getString("token");
            if (userToken.equals(candidateToken)) {
                hasUser = true;
                setUserOnlineStatus(candidateToken, "true");
                break;
            }
        }

        if (hasUser == false) {
            users.put(candidate);
            return true;
        }
        else {
            return false;
        }
    }

    boolean putActiveUser(JSONObject candidate) {
        JSONArray usersActive = getActiveUsers();
        JSONArray users = getUsers();
        JSONObject obj;
        String userToken;
        String candidateToken;
        boolean hasActiveUser = false;
        candidateToken = candidate.getString("token");

        for (int i = 0; i < usersActive.length(); i++) {
            obj = (JSONObject)users.get(i);
            userToken = obj.getString("token");
            if (userToken.equals(candidateToken)) {
                hasActiveUser = true;
                break;
            }
        }

        if (hasActiveUser == true) {
            return false;
        }

        for (int i = 0; i < users.length(); i++) {
            obj = (JSONObject)users.get(i);
            userToken = obj.getString("token");
            if (userToken.equals(candidateToken)) {
                usersActive.put(obj);
                return true;
            }
        }

        usersActive.put(candidate);
        return true;
    }

    void setUserOnlineStatus(String token, String status) {
        JSONArray users = getUsers();

        for (int i = 0; i < users.length(); i++) {
            JSONObject obj = (JSONObject)users.get(i);
            if (token.equals(obj.getString("token"))) {
                users.remove(i);
                obj.remove("online");
                obj.put("online", status);
                users.put(obj);
            }
        }
    }

    void setActiveUsers(JSONArray newArray) {
        usersActive = newArray;
    }

    JSONObject getActiveUser(String candidateToken) {
        JSONArray usersActive = getActiveUsers();
        JSONObject obj;

        for (int i = 0; i < usersActive.length(); i++) {
            obj = (JSONObject)usersActive.get(i);
            if (candidateToken.equals(obj.getString("token"))) {
                return obj;
            }
        }

        return null;
    }

    JSONObject getActiveUser(int candidateID) {
        JSONArray usersActive = getUsers();
        JSONObject obj;

        for (int i = 0; i < usersActive.length(); i++) {
            obj = (JSONObject)usersActive.get(i);
            if (candidateID == obj.getInt("id")) {
                return obj;
            }
        }

        return null;
    }

    JSONArray getUsers() {
        return users;
    }

    JSONArray getActiveUsers() {
        return usersActive;
    }

    JSONArray getMessages() { return messages; }

    boolean deleteActiveUser(String userToken) {
        JSONArray array = getActiveUsers();
        JSONObject obj;

        for (int i = 0; i < array.length(); i++) {
            obj = (JSONObject)array.get(i);
            String currentToken = obj.getString("token");
            if (currentToken.equals(userToken)) {
                array.remove(i);
                setUserOnlineStatus(currentToken, "false");
                return true;
            }
        }

        return false;
    }
}
