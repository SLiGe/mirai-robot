package cn.zjiali.robot.handler;

import cn.zjiali.robot.config.PluginConfig;
import cn.zjiali.robot.constant.ServerUrl;
import cn.zjiali.robot.entity.response.FortuneResponse;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import com.google.gson.JsonObject;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;

/**
 * 运势消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 20:58
 */
public class FortuneHandler implements Handler {

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String msgString = event.getMessage().contentToString();
        long qq = event.getSender().getId();
        long groupNum = event.getGroup().getId();
        if ("运势".equals(msgString)) {
            String fortuneMsg = getFortuneMsg(qq, groupNum, 2);
            if (fortuneMsg != null) {
                event.getGroup().sendMessage(new At(event.getSender()).plus(fortuneMsg));
            }
        }
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        String msgString = event.getMessage().contentToString();
        long qq = event.getSender().getId();
        if ("运势".equals(msgString)) {
            String fortuneMsg = getFortuneMsg(qq, 0, 1);
            if (fortuneMsg != null) {
                event.getSender().sendMessage(fortuneMsg);
            }
        }
    }

    private String getFortuneMsg(long senderQQ, long groupNum, int msgType) {
        StringBuilder msgContentBuilder = new StringBuilder();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", Long.toString(senderQQ));
        jsonObject.addProperty("isOne", PluginConfig.fortune_day_one);
        jsonObject.addProperty("isGroup", msgType == 1 ? 0 : 1);
        jsonObject.addProperty("isIntegral", PluginConfig.fortune_point);
        jsonObject.addProperty("groupNum", Long.toString(groupNum));
        String response = HttpUtil.httpPost(ServerUrl.FORTUNE_URL, jsonObject);
        System.out.println(response);
        if (response == null) {
            return "运势服务故障,请联系管理员!";
        }
        JsonObject resObj = JsonUtil.json2obj(response, JsonObject.class);
        final String status = resObj.get("status").getAsString();
        if ("40010".equals(status)) {
            return null;
        }
        if ("20011".equals(status)) {
            String data = resObj.get("data").toString();
            FortuneResponse fortuneResponse = JsonUtil.json2obj(data, FortuneResponse.class);
            msgContentBuilder.append("\uD83C\uDF13您的今日运势为：").append(fortuneResponse.getFortuneSummary());
            if ("1".equals(PluginConfig.fortune_star_num))
                msgContentBuilder.append("\n\uD83C\uDF1F星指数：").append(fortuneResponse.getLuckyStar());
            if ("1".equals(PluginConfig.fortune_sign_text))
                msgContentBuilder.append("\n\uD83D\uDCD7签文：").append(fortuneResponse.getSignText());
            if ("1".equals(PluginConfig.fortune_un_sign))
                msgContentBuilder.append("\n\uD83D\uDCDD解签：").append(fortuneResponse.getUnSignText());

        }
        return msgContentBuilder.toString();
    }
}
