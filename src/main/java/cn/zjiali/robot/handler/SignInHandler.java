package cn.zjiali.robot.handler;

import cn.zjiali.robot.constant.ServerUrl;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import com.google.gson.JsonObject;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;

import java.util.Random;

/**
 * @author zJiaLi
 * @since 2020-10-29 20:57
 */
public class SignInHandler implements Handler {
    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        long senderQQ = event.getSender().getId();
        long groupNum = event.getGroup().getId();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", Long.toString(senderQQ));
        jsonObject.addProperty("group", Long.toString(groupNum));
        // 2群组消息
        jsonObject.addProperty("msgType", 2);
        int point = new Random(20).nextInt(60);
        jsonObject.addProperty("integral", Integer.toString(point));
        String signInDataJson = HttpUtil.httpPost(ServerUrl.SIGN_IN_URL, jsonObject);
        JsonObject signInData = JsonUtil.json2obj(signInDataJson);
        String status = signInData.get("status").getAsString();
        //签到成功200  已经签到203
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {

    }
}
