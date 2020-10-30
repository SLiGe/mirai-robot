package cn.zjiali.robot.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * @author zJiaLi
 * @since 2020-10-30 14:54
 */
public class JsonUtil {

    private static final Gson GSON = new Gson();

    private JsonUtil() {
    }

    public static String obj2str(Object obj) {
        return GSON.toJson(obj);
    }

    public static JsonObject json2obj(String json){
        return GSON.fromJson(json,JsonObject.class);
    }
}
