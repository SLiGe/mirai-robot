package cn.zjiali.robot.net;

import okhttp3.*;

import java.util.*;

/**
 * @author zJiaLi
 * @since 2020-10-29 11:34
 */
public class HttpUtil {

    private static final OkHttpClient okHttpClient = new OkHttpClient();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
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

}
