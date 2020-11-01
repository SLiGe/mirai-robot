package cn.zjiali.robot.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

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

    public static <T>T json2obj(String json,Class t){

        return GSON.fromJson(json, (Type) t);
    }
}
