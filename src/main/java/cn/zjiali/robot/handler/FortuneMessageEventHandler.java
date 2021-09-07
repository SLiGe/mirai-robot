package cn.zjiali.robot.handler;

import cn.zjiali.robot.constant.AppConstants;
import cn.zjiali.robot.util.PluginConfigUtil;
import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.constant.PluginProperty;
import cn.zjiali.robot.factory.MessageFactory;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.model.response.FortuneResponse;
import cn.zjiali.robot.model.response.RobotBaseResponse;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;

import java.lang.reflect.Type;


/**
 * 运势消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 20:58
 */
public class FortuneMessageEventHandler extends AbstractMessageEventHandler {

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        long qq = event.getSender().getId();
        long groupNum = event.getGroup().getId();
        String fortuneMsg = MessageFactory.getFortuneMsg(qq, groupNum, 2);
        if (fortuneMsg != null) {
            event.getGroup().sendMessage(new At(event.getSender().getId()).plus(fortuneMsg));
        }
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        long qq = event.getSender().getId();
        String fortuneMsg = MessageFactory.getFortuneMsg(qq, 0, 1);
        if (fortuneMsg != null) {
            event.getSender().sendMessage(fortuneMsg);
        }
    }


    /**
     * 获取运势消息
     *
     * @param senderQQ 发送人QQ
     * @param groupNum 群组
     * @param msgType  消息类型
     * @return
     */
    private OutMessage getFortuneMsg(long senderQQ, long groupNum, int msgType) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", Long.toString(senderQQ));
        jsonObject.addProperty("isOne", PluginConfigUtil.getConfigVal(PluginCode.FORTUNE, PluginProperty.FORTUNE_DAY_ONE));
        jsonObject.addProperty("isGroup", msgType == 1 ? 0 : 1);
        jsonObject.addProperty("isIntegral", PluginConfigUtil.getConfigVal(PluginCode.FORTUNE, PluginProperty.FORTUNE_POINT));
        jsonObject.addProperty("groupNum", Long.toString(groupNum));
        String response = HttpUtil.post(PluginConfigUtil.getApiURL(PluginCode.FORTUNE), jsonObject);
        if (response == null) {
            return OutMessage.builder().convertFlag(false).content("运势服务故障,请联系管理员!").build();
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
            return OutMessage.builder().convertFlag(true).templateCode(PluginCode.FORTUNE).fillFlag(AppConstants.FILL_OUT_MESSAGE_OBJECT_FLAG)
                    .fillObj(dataResponse).build();

        }
        return null;
    }

    @Override
    public OutMessage handleGroupMessageEvent(GroupMessageEvent event) {
        long qq = event.getSender().getId();
        long groupNum = event.getGroup().getId();
        return getFortuneMsg(qq, groupNum, 2);
    }

    @Override
    public OutMessage handleFriendMessageEvent(FriendMessageEvent event) {
        long qq = event.getSender().getId();
        return getFortuneMsg(qq, 0, 1);
    }

    @Override
    public boolean next() {
        return false;
    }

    @Override
    public boolean matchCommand(String msg) {
        return containCommand(PluginCode.FORTUNE, msg);
    }
}
