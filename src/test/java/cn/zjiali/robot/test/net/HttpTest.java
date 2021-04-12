package cn.zjiali.robot.test.net;

import cn.zjiali.robot.constant.ServerUrl;
import cn.zjiali.robot.entity.response.CsylqResponse;
import cn.zjiali.robot.entity.response.JuHeBaseResponse;
import cn.zjiali.robot.entity.response.TodayOnHistoryResponse;
import cn.zjiali.robot.factory.AsyncFactory;
import cn.zjiali.robot.main.AsyncManager;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.TimerTask;

/**
 * @author zJiaLi
 * @since 2020-10-30 14:47
 */
public class HttpTest {

    public static void main(String[] args) {
        //testParseResponse();
        //testCslq();
        testThreadPool();
    }

    public static void testThreadPool() {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            AsyncManager.me().execute(new TimerTask() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (finalI == 4) {
                     //   throw new NullPointerException();
                    }
                    System.out.println("222222");
                }
            });
            AsyncManager.me().execute(AsyncFactory.sendResponse("sss", "sss"));
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testSignIn() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", "357078415");
        jsonObject.addProperty("integral", "20");
        String post = HttpUtil.httpPost(ServerUrl.SIGN_IN_URL, jsonObject);
        System.out.println(post);
    }

    private static void testCslq() {
        String res = HttpUtil.httpGet("http://i.itpk.cn/api.php?question=财神爷灵签");
        CsylqResponse o = JsonUtil.json2obj(res, CsylqResponse.class);
        System.out.println(o);
    }

    private static void testParseResponse() {
        String response = "{\n" +
                "    \"reason\": \"success\",\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"day\": \"1/1\",\n" +
                "            \"date\": \"前45年01月01日\",\n" +
                "            \"title\": \"罗马共和国开始使用儒略历\",\n" +
                "            \"e_id\": \"1\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"error_code\": 0\n" +
                "}";
        JuHeBaseResponse<TodayOnHistoryResponse> todayOnHistoryResponseJuHeBaseResponse = new JuHeBaseResponse<>();
        try {
            Type type = new TypeToken<JuHeBaseResponse<List<TodayOnHistoryResponse>>>() {
            }.getType();
            JuHeBaseResponse<List<TodayOnHistoryResponse>> juHeBaseResponse2 = JsonUtil.toObjByType(response, type);
            JuHeBaseResponse<List<TodayOnHistoryResponse>> o = new Gson().fromJson(response, type);
            JuHeBaseResponse juHeBaseResponse = JsonUtil.getGson().fromJson(response, JuHeBaseResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
