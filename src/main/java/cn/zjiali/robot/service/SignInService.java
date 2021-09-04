package cn.zjiali.robot.service;

import cn.zjiali.robot.annotation.Service;
import cn.zjiali.robot.util.PluginConfigUtil;
import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.constant.PluginProperty;
import cn.zjiali.robot.model.response.RobotBaseResponse;
import cn.zjiali.robot.model.response.SignInDataResponse;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
        String signInDataJson = HttpUtil.post(PluginConfigUtil.getConfigVal(PluginCode.SIGN, PluginProperty.SIGN_URL), jsonObject);
        Type type = new TypeToken<RobotBaseResponse<SignInDataResponse>>() {
        }.getType();
        RobotBaseResponse<SignInDataResponse> robotBaseResponse = JsonUtil.toObjByType(signInDataJson, type);
        if (robotBaseResponse.getStatus() == 500) {
            return null;
        }
        SignInDataResponse signInDataResponse = robotBaseResponse.getData();
        if (signInDataResponse.getStatus() == 203) {
            return signInDataResponse;
        }
        signInDataResponse.setGetPoints(point);
        return signInDataResponse;
    }

    public SignInDataResponse getSignInData(String qq, String group, int msgType) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", qq);
        jsonObject.addProperty("group", group);
        // 1好友消息 2群组消息
        jsonObject.addProperty("msgType", msgType);
        String signInDataJson = HttpUtil.post(PluginConfigUtil.getConfigVal(PluginCode.SIGN, PluginProperty.QUERY_SIGN_URL), jsonObject);
        Type type = new TypeToken<RobotBaseResponse<SignInDataResponse>>() {
        }.getType();
        RobotBaseResponse<SignInDataResponse> robotBaseResponse = JsonUtil.toObjByType(signInDataJson, type);
        if (robotBaseResponse.getStatus() == 200) {
            return robotBaseResponse.getData();
        }
        return null;
    }
}
