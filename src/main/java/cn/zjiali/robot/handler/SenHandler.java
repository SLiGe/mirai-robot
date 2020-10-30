package cn.zjiali.robot.handler;

import cn.zjiali.robot.config.ServerUrlConfig;
import cn.zjiali.robot.net.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;

/**
 * 一言处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 20:57
 */
public class SenHandler implements Handler {

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        event.getGroup().sendMessage(getSen());
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        event.getFriend().sendMessage(getSen());
    }

    private String getSen() {
        String sen = HttpUtil.httpGet(ServerUrlConfig.SEN_URL);
        JsonObject jsonObject = new Gson().fromJson(sen, JsonObject.class);
        return jsonObject.get("data").getAsString();
    }
}
