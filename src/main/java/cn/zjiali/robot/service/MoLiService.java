package cn.zjiali.robot.service;

import cn.zjiali.robot.annotation.Service;
import cn.zjiali.robot.config.plugin.MoLiConfig;
import cn.zjiali.robot.entity.response.CsylqResponse;
import cn.zjiali.robot.entity.response.GylqResponse;
import cn.zjiali.robot.entity.response.JokeResponse;
import cn.zjiali.robot.entity.response.YllqResponse;
import cn.zjiali.robot.factory.AsyncFactory;
import cn.zjiali.robot.main.AsyncManager;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.MessageUtil;
import cn.zjiali.robot.util.PropertiesUtil;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zJiaLi
 * @since 2021-04-08 12:49
 */
@Service
public class MoLiService {

    private String sendResponseFlag = "";

    public MoLiService() {
        try {
            this.sendResponseFlag = PropertiesUtil.getProperty("application.properties", "application.receive.data.send");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCommonChatMessage(String msg) {
        Map<String, Object> paramMap = new HashMap<>();

        if ("1".equals(MoLiConfig.isMoLiServer)) {
            paramMap.put("question", msg);
            if ("".equals(MoLiConfig.limit)) {
                paramMap.put("limit", "5");
            } else {
                paramMap.put("limit", MoLiConfig.limit);
            }
            if (!"".equals(MoLiConfig.apiKey)) {
                paramMap.put("api_key", MoLiConfig.apiKey);
            }
            if (!"".equals(MoLiConfig.apiSecret)) {
                paramMap.put("api_secret", MoLiConfig.apiSecret);
            }
            return HttpUtil.httpGet(MoLiConfig.url, paramMap);
        } else {
            paramMap.put("message", msg);
        }
        return HttpUtil.httpPost(MoLiConfig.zUrlChat, paramMap);

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
        String jokeContent = HttpUtil.httpGet(MoLiConfig.zUrlJoke, paramMap);
        JokeResponse jokeResponse = JsonUtil.json2obj(jokeContent, JokeResponse.class);
        if ("".equals(MoLiConfig.jokeTemplate)) {
            return jokeResponse.getContent();
        }
        return MessageUtil.replaceMessage(MoLiConfig.jokeTemplate, jokeResponse);

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
        GylqResponse gylqResponse = JsonUtil.json2obj(gylqJson, GylqResponse.class);
        return MessageUtil.replaceMessage(MoLiConfig.gylqTemplate, gylqResponse);
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
        YllqResponse yllqResponse = JsonUtil.json2obj(yllqJson, YllqResponse.class);
        return MessageUtil.replaceMessage(MoLiConfig.yllqTemplate, yllqResponse);
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
        CsylqResponse csylqResponse = JsonUtil.json2obj(csylqJson, CsylqResponse.class);
        return MessageUtil.replaceMessage(MoLiConfig.csylqTemplate, csylqResponse);
    }

    public String queryLqData(int type, long qq, boolean isGroup, long groupNum) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("type", type);
        paramMap.put("qq", qq);
        paramMap.put("groupNum", groupNum);
        paramMap.put("isGroup", isGroup ? 1 : 0);
        return HttpUtil.httpPost(MoLiConfig.zUrlLq, paramMap);
    }
}
