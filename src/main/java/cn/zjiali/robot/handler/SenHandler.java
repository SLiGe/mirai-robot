package cn.zjiali.robot.handler;

import cn.zjiali.robot.constant.ServerUrl;
import cn.zjiali.robot.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

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
        String sen = HttpUtil.httpGet(ServerUrl.SEN_URL);
        JsonObject jsonObject = new Gson().fromJson(sen, JsonObject.class);
        return jsonObject.get("data").getAsString();
    }
}
