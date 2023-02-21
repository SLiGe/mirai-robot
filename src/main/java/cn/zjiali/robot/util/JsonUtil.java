package cn.zjiali.robot.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author zJiaLi
 * @since 2020-10-30 14:54
 */
public class JsonUtil {

    private static final Gson GSON = generateGson();

    private JsonUtil() {
    }

    private static Gson generateGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }

    public static Gson getGson() {
        return GSON;
    }

    public static String obj2str(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T json2obj(String json, Class<T> t) {

        return GSON.fromJson(json, (Type) t);
    }

    public static <T> List<T> toList(String json, Class<T> t) {
        return GSON.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    public static <T> T toObjByType(String json, Type type) {
        return GSON.fromJson(json, type);
    }
}
