package com.zhonghuipro.zhuge.api.helper;
import org.json.*;

public class JsonUtils {
    public static JSONObject getJsonObject(String jsonString){
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
//            e.printStackTrace();
            return null;
        }
    }

}
