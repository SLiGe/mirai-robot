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
        Map<String, String> paramMap = new HashMap<>();
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

    }

    /**
     * 获取笑话
     *
     * @return
     */
    public String getJokeMessage() {
        String jokeJson = getCommonChatMessage("笑话");
        JokeResponse jokeResponse = JsonUtil.json2obj(jokeJson, JokeResponse.class);
        AsyncManager.me().execute(sendResponseFlag, AsyncFactory.sendResponse("joke", jokeJson));
        if ("".equals(MoLiConfig.jokeTemplate)) {
            return jokeResponse.getContent();
        }
        return MessageUtil.replaceMessage(MoLiConfig.jokeTemplate, jokeResponse);

    }

    /**
     * 获取观音灵签
     *
     * @return
     */
    public String getGylqMessage() {
        String gylqJson = getCommonChatMessage("观音灵签");
        AsyncManager.me().execute(sendResponseFlag, AsyncFactory.sendResponse("gylq", gylqJson));
        GylqResponse gylqResponse = JsonUtil.json2obj(gylqJson, GylqResponse.class);
        return MessageUtil.replaceMessage(MoLiConfig.gylqTemplate, gylqResponse);
    }

    /**
     * 获取月老灵签
     *
     * @return
     */
    public String getYllqMessage() {
        String yllqJson = getCommonChatMessage("月老灵签");
        AsyncManager.me().execute(sendResponseFlag, AsyncFactory.sendResponse("yllq", yllqJson));
        YllqResponse yllqResponse = JsonUtil.json2obj(yllqJson, YllqResponse.class);
        return MessageUtil.replaceMessage(MoLiConfig.yllqTemplate, yllqResponse);
    }

    /**
     * 获取月老灵签
     *
     * @return
     */
    public String getCsylqMessage() {
        String csylqJson = getCommonChatMessage("财神爷灵签");
        AsyncManager.me().execute(sendResponseFlag, AsyncFactory.sendResponse("csylq", csylqJson));
        CsylqResponse csylqResponse = JsonUtil.json2obj(csylqJson, CsylqResponse.class);
        return MessageUtil.replaceMessage(MoLiConfig.csylqTemplate, csylqResponse);
    }
}
