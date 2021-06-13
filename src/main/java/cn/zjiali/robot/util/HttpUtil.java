package cn.zjiali.robot.util;

import com.google.gson.JsonObject;
import net.mamoe.mirai.utils.MiraiLogger;
import net.mamoe.mirai.utils.PlatformLogger;
import okhttp3.*;

import java.io.IOException;
import java.util.*;

/**
 * @author zJiaLi
 * @since 2020-10-29 11:34
 */
public class HttpUtil {

    private static final MiraiLogger miraiLogger = new PlatformLogger(HttpUtil.class.getName());

    private static final OkHttpClient okHttpClient = new OkHttpClient();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String CONTENT_TYPE = "Content-Type";

    /**
     * get请求
     *
     * @param url    请求地址
     * @param params 参数
     * @return
     */
    public static String httpGet(String url, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            Set<String> keySet = params.keySet();
            sb.append(url).append("?");
            int index = 0;
            for (String key : keySet) {
                sb.append(key).append("=").append(params.get(key));
                if (index < (params.size() - 1)) {
                    sb.append("&");
                }
                index++;
            }
            return httpGet(sb.toString());
        }
        return httpGet(url);
    }

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        miraiLogger.debug("[httpGet]====请求URL: " + url);
        String result = null;
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.body() != null) {
                result = Objects.requireNonNull(response.body()).string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String httpPost(String url, JsonObject data) {
        return httpPost(url, JsonUtil.obj2str(data));
    }

    public static String httpPost(String url, Map<String, Object> params) {
        return httpPost(url, JsonUtil.obj2str(params));
    }

    public static String httpPost(String url, String data) {
        miraiLogger.debug("[httpPost]====请求URL: " + url);
        RequestBody requestBody = RequestBody.create(data, JSON);
        Request request = new Request.Builder().url(url)
                .header("token", "294a4fd5929bf405060d14566a72b18a")
                .post(requestBody).build();
        String result = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.body() != null) {
                result = Objects.requireNonNull(response.body()).string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



}
