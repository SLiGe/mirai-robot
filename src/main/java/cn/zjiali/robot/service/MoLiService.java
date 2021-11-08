package cn.zjiali.robot.service;

import cn.zjiali.robot.annotation.Service;
import cn.zjiali.robot.config.PluginTemplate;
import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.constant.PluginProperty;
import cn.zjiali.robot.model.response.*;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.MessageUtil;
import cn.zjiali.robot.util.PluginConfigUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zJiaLi
 * @since 2021-04-08 12:49
 */
@Service
public class MoLiService {

    public String getCommonChatMessage(String msg) {
        Map<String, Object> paramMap = new HashMap<>();
        String isMoLiServer = PluginConfigUtil.getConfigVal(PluginCode.MOLI, PluginProperty.IS_MOLI_SERVER);
        if ("1".equals(isMoLiServer)) {
            paramMap.put("question", msg);
            String limit = PluginConfigUtil.getConfigVal(PluginCode.MOLI, PluginProperty.MOLI_LIMIT);
            if ("".equals(limit)) {
                paramMap.put("limit", "5");
            } else {
                paramMap.put("limit", limit);
            }
            String apiKey = PluginConfigUtil.getConfigVal(PluginCode.MOLI, PluginProperty.MOLI_API_KEY);
            if (!"".equals(apiKey)) {
                paramMap.put("api_key", apiKey);
            }
            String apiSecret = PluginConfigUtil.getConfigVal(PluginCode.MOLI, PluginProperty.MOLI_API_SECRET);
            if (!"".equals(apiSecret)) {
                paramMap.put("api_secret", apiSecret);
            }
            return HttpUtil.get(PluginConfigUtil.getApiURL(PluginCode.MOLI), paramMap);
        } else {
            paramMap.put("message", msg);
        }
        String zUrlChat = PluginConfigUtil.getConfigVal(PluginCode.MOLI, PluginProperty.Z_URL_CHAT);
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
        String apiURL = PluginConfigUtil.getApiURL(PluginCode.JOKE);
        String jokeContent = HttpUtil.get(apiURL, paramMap);
        Type type = new TypeToken<RobotBaseResponse<JokeResponse>>() {
        }.getType();
        RobotBaseResponse<JokeResponse> jokeResponse = JsonUtil.toObjByType(jokeContent, type);
        return jokeResponse.getData().getContent();

    }

    /**
     * 获取观音灵签
     *
     * @param qq
     * @param isGroup
     * @param groupNum
     * @return
     */
    public String getGylqMessage(long qq, boolean isGroup, long groupNum) {
        String gylqJson = queryLqData(1, qq, isGroup, groupNum);
        GylqResponse gylqResponse = getChatResponse(gylqJson, GylqResponse.class);
        String template = PluginTemplate.getInstance().getTemplate(PluginCode.GY_LQ);
        return MessageUtil.replaceMessage(template, gylqResponse);
    }

    /**
     * 获取月老灵签
     *
     * @param qq
     * @param isGroup
     * @param groupNum
     * @return
     */
    public String getYllqMessage(long qq, boolean isGroup, long groupNum) {
        String yllqJson = queryLqData(2, qq, isGroup, groupNum);
        YllqResponse yllqResponse = getChatResponse(yllqJson, YllqResponse.class);
        String template = PluginTemplate.getInstance().getTemplate(PluginCode.YL_LQ);
        return MessageUtil.replaceMessage(template, yllqResponse);
    }

    /**
     * 获取月老灵签
     *
     * @param qq
     * @param isGroup
     * @param groupNum
     * @return
     */
    public String getCsylqMessage(long qq, boolean isGroup, long groupNum) {
        String csylqJson = queryLqData(3, qq, isGroup, groupNum);
        CsylqResponse csylqResponse = getChatResponse(csylqJson, CsylqResponse.class);
        String template = PluginTemplate.getInstance().getTemplate(PluginCode.CSY_LQ);
        return MessageUtil.replaceMessage(template, csylqResponse);
    }

    public String queryLqData(int type, long qq, boolean isGroup, long groupNum) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("type", type);
        paramMap.put("qq", qq);
        paramMap.put("groupNum", groupNum);
        paramMap.put("isGroup", isGroup ? 1 : 0);
        String zUrlLq = PluginConfigUtil.getConfigVal(PluginCode.MOLI, PluginProperty.Z_URL_LQ);
        return HttpUtil.post(zUrlLq, paramMap);
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
