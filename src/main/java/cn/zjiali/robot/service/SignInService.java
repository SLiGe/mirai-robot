package cn.zjiali.robot.service;

import cn.zjiali.robot.annotation.Service;
import cn.zjiali.robot.constant.ServerUrl;
import cn.zjiali.robot.entity.response.SignInDataResponse;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import com.google.gson.JsonObject;

import java.util.Random;

/**
 * 签到服务类
 *
 * @author zJiaLi
 * @since 2020-10-30 22:06
 */
@Service
public class SignInService {

    public SignInDataResponse doSignIn(String qq, String group, int msgType) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", qq);
        jsonObject.addProperty("group", group);
        // 1好友消息 2群组消息
        jsonObject.addProperty("msgType", msgType);
        int point = new Random(20).nextInt(60);
        jsonObject.addProperty("points", (point));
        String signInDataJson = HttpUtil.httpPost(ServerUrl.SIGN_IN_URL, jsonObject);
        JsonObject jsonObj = JsonUtil.json2obj(signInDataJson, JsonObject.class);
        int status = jsonObj.get("status").getAsInt();
        if (status == 500) {
            return null;
        }
        JsonObject dataJsonObject = jsonObj.get("data").getAsJsonObject();
        int signInStatus = dataJsonObject.get("status").getAsInt();
        if (signInStatus == 203) {
            SignInDataResponse signInDataResponse = new SignInDataResponse();
            signInDataResponse.setStatus("203");
            return signInDataResponse;
        }
        String data = dataJsonObject.get("getSignInDataResponse").toString();
        SignInDataResponse signInDataResponse = JsonUtil.json2obj(data, SignInDataResponse.class);
        signInDataResponse.setStatus(String.valueOf(status));
        signInDataResponse.setGetPoints(point);
        return signInDataResponse;
    }

    public SignInDataResponse getSignInData(String qq, String group, int msgType) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", qq);
        jsonObject.addProperty("group", group);
        // 1好友消息 2群组消息
        jsonObject.addProperty("msgType", msgType);
        String signInDataJson = HttpUtil.httpPost(ServerUrl.SIGN_IN_DATA_URL, jsonObject);
        JsonObject jsonObj = JsonUtil.json2obj(signInDataJson, JsonObject.class);
        int status = jsonObj.get("status").getAsInt();
        if (status == 200) {
            JsonObject dataJsonObject = jsonObj.get("data").getAsJsonObject();
            String data = dataJsonObject.get("getSignInDataResponse").getAsString();
            SignInDataResponse signInDataResponse = JsonUtil.json2obj(data, SignInDataResponse.class);
            signInDataResponse.setStatus(String.valueOf(status));
            return signInDataResponse;
        }
        return null;
    }
}
