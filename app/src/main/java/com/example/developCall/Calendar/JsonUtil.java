package com.example.developCall.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    public static String toJSon(CalendarData data) {
        try {

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("title", data.getProfile());
            jsonObj.put("date", data.getName());
            jsonObj.put("note", data.getNumber());

            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;

    }
}
