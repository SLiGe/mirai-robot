package cn.zjiali.robot.factory;

import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.constant.PluginProperty;
import cn.zjiali.robot.model.response.FortuneResponse;
import cn.zjiali.robot.model.response.RobotBaseResponse;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.MessageUtil;
import cn.zjiali.robot.util.PluginConfigUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * 消息工厂
 *
 * @author zJiaLi
 * @since 2021-04-04 21:04
 */
public class MessageFactory {


    /**
     * 获取运势消息
     *
     * @param senderQQ 发送人QQ
     * @param groupNum 群组
     * @param msgType  消息类型
     * @return
     */
    public static String getFortuneMsg(long senderQQ, long groupNum, int msgType) {
        String msgContentBuilder = "";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", Long.toString(senderQQ));
        jsonObject.addProperty("isOne", PluginConfigUtil.getConfigVal(PluginCode.FORTUNE, PluginProperty.FORTUNE_DAY_ONE));
        jsonObject.addProperty("isGroup", msgType == 1 ? 0 : 1);
        jsonObject.addProperty("isIntegral", PluginConfigUtil.getConfigVal(PluginCode.FORTUNE, PluginProperty.FORTUNE_POINT));
        jsonObject.addProperty("groupNum", Long.toString(groupNum));
        String response = HttpUtil.post(PluginConfigUtil.getApiURL(PluginCode.FORTUNE), jsonObject);
        if (response == null) {
            return "运势服务故障,请联系管理员!";
        }
        Type type = new TypeToken<RobotBaseResponse<FortuneResponse>>() {
        }.getType();
        RobotBaseResponse<FortuneResponse> robotBaseResponse = JsonUtil.toObjByType(response, type);
        if (robotBaseResponse.getStatus() == 500) { //server has  error
            return null;
        }
        FortuneResponse responseData = robotBaseResponse.getData();
        int dataStatus = responseData.getStatus();
        if (dataStatus == 201) {
            return null; // already get fortune
        }
        if (dataStatus == 200) {
            FortuneResponse.DataResponse dataResponse = responseData.getDataResponse();
            return MessageUtil.replaceMessage(PluginConfigUtil.getTemplate(PluginCode.FORTUNE), dataResponse);

        }
        return msgContentBuilder;
    }


    /**
     * 获取一言
     *
     * @return
     */
    public static String getSen() {
        String sen = HttpUtil.get(PluginConfigUtil.getApiURL(PluginCode.ONE_SEN));
        JsonObject jsonObject = new Gson().fromJson(sen, JsonObject.class);
        return jsonObject.get("data").getAsString();
    }
}
