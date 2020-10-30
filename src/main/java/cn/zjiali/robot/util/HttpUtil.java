package cn.zjiali.robot.util;

import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.util.*;

/**
 * @author zJiaLi
 * @since 2020-10-29 11:34
 */
public class HttpUtil {

    private static final OkHttpClient okHttpClient = new OkHttpClient();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String CONTENT_TYPE = "Content-Type";

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static String httpGet(String url) {
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
        RequestBody requestBody = RequestBody.create(JsonUtil.obj2str(data), JSON);
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
