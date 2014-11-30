package com.zzzmode.jobwork.utils;

import com.google.gson.Gson;

/**
 * Created by zl on 2014/11/25.
 */
public class JsonUtil {
    private JsonUtil(){}

    private static Gson gson;

    public static Gson getGson(){
        if(gson == null) {
            synchronized (JsonUtil.class) {
                gson = new Gson();
            }
        }
        return gson;
    }
}
