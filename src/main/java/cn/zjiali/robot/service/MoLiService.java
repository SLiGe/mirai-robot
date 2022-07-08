package cn.zjiali.robot.service;

import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.constant.PluginProperty;
import cn.zjiali.robot.manager.PluginManager;
import cn.zjiali.robot.model.response.JokeResponse;
import cn.zjiali.robot.model.response.RobotBaseResponse;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zJiaLi
 * @since 2021-04-08 12:49
 */
@Singleton
public class MoLiService {

    @Inject
    private PluginManager pluginManager;

    public String getCommonChatMessage(long groupId, long senderId, String msg) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("message", msg);
        paramMap.put("qq", Long.toString(senderId));
        String zUrlChat = pluginManager.getConfigVal(PluginCode.MOLI, PluginProperty.Z_URL_CHAT, groupId, senderId);
        String reply = HttpUtil.post(zUrlChat, paramMap);
        return getChatResponse(reply, String.class);

    }

    /**
     * 获取笑话
     *
     * @param qq
     * @param isGroup
     * @param groupNum
     * @return
     */
    public String getJokeMessage(long qq, boolean isGroup, long groupNum) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("qq", qq);
        paramMap.put("groupNum", groupNum);
        paramMap.put("isGroup", isGroup ? 1 : 0);
        String apiURL = pluginManager.getApiURL(PluginCode.JOKE, groupNum, qq);
        String jokeContent = HttpUtil.get(apiURL, paramMap);
        Type type = new TypeToken<RobotBaseResponse<JokeResponse>>() {
        }.getType();
        RobotBaseResponse<JokeResponse> jokeResponse = JsonUtil.toObjByType(jokeContent, type);
        return jokeResponse.getData().getContent();

    }


    public <T> T getChatResponse(String json, Class<T> tClass) {
        RobotBaseResponse<T> response;
        if (tClass == String.class) {
            response = JsonUtil.toObjByType(json, new TypeToken<RobotBaseResponse<String>>() {
            }.getType());
        } else {
            response = toBaseResponse(json, tClass);
        }
        return response.getData();
    }


    public <T> RobotBaseResponse<T> toBaseResponse(String json, Class<T> tClass) {
        Type type = new TypeToken<RobotBaseResponse<String>>() {
        }.getType();
        RobotBaseResponse<String> originResponse = JsonUtil.toObjByType(json, type);
        RobotBaseResponse<T> realResponse = new RobotBaseResponse<>();
        realResponse.setStatus(originResponse.getStatus());
        realResponse.setMessage(originResponse.getMessage());
        T dataInstance = JsonUtil.json2obj(originResponse.getData(), tClass);
        realResponse.setData(dataInstance);
        return realResponse;
    }
}
