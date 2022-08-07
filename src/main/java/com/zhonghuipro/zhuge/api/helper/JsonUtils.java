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

    public static String getJsonString(JSONObject jsonObject, String name){
        if (jsonObject == null){
            return "";
        }
        try {
            return jsonObject.getString(name);
        } catch (JSONException e) {
            return "";
        }
    }

}
