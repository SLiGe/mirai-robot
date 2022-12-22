package cn.zjiali.robot.util;

import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.manager.ServerTokenManager;
import com.google.gson.JsonObject;
import net.mamoe.mirai.utils.MiraiLogger;
import net.mamoe.mirai.utils.PlatformLogger;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author zJiaLi
 * @since 2020-10-29 11:34
 */
public class HttpUtil {

    private static final MiraiLogger miraiLogger = new PlatformLogger(HttpUtil.class.getName());

    private static final OkHttpClient okHttpClient;

    static {
        okHttpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.addHeader("Client-Type", "robot");
            if (!chain.request().url().toString().equals(AppConfig.getApplicationConfig().getServerUrl())) {
                ServerTokenManager serverTokenManager = GuiceUtil.getBean(ServerTokenManager.class);
                requestBuilder.addHeader("Authorization", serverTokenManager.serverToken());
                requestBuilder.addHeader("Cookie", "Admin-Token=" + serverTokenManager.serverToken());
            }
            return chain.proceed(requestBuilder.build());
        }).callTimeout(Duration.ofSeconds(25)).connectTimeout(Duration.ofSeconds(15)).build();
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String CONTENT_TYPE = "Content-Type";

    /**
     * get请求
     *
     * @param url    请求地址
     * @param params 参数
     * @return
     */
    public static String get(String url, Map<String, Object> params) {
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
            return get(sb.toString());
        }
        return get(url);
    }

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        miraiLogger.debug("[httpGet]====请求URL: " + url);
        String result = null;
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.body() != null) {
                result = Objects.requireNonNull(response.body()).string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static InputStream fileStream(String url) {
        miraiLogger.debug("[httpGet]====请求URL: " + url);
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).byteStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String post(String url, JsonObject data) {
        return post(url, JsonUtil.obj2str(data));
    }

    public static String post(String url, Map<String, Object> params) {
        return post(url, JsonUtil.obj2str(params));
    }

    public static String post(String url, String data) {
        miraiLogger.debug("[httpPost]====请求URL: " + url);
        RequestBody requestBody = RequestBody.create(data, JSON);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        String result = null;
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.body() != null) {
                result = Objects.requireNonNull(response.body()).string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
