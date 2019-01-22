package ChatServerWS;

import io.undertow.server.HttpServerExchange;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

public class Utils {
    public static String readFromScanner(Scanner sc) {
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

        return sResult;
    }

    public static String formJSON(JSONArray jsonArray, StringBuffer sBuffer) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = (JSONObject) jsonArray.get(i);
            sBuffer.append(obj.toString());
            if (i < jsonArray.length() - 1) {
                sBuffer.append(',');
            }
        }

        sBuffer.append("]}");

        return sBuffer.toString();
    }

    public static void endExchangeWithErrorCode(HttpServerExchange he, int code) {
        he.setResponseCode(code);
        he.setResponseContentLength(0);
        he.endExchange();
    }
}

//Utils.endExchangeWithErrorCode
