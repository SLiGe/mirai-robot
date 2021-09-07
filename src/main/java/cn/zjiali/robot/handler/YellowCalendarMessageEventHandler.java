package cn.zjiali.robot.handler;

import cn.zjiali.robot.constant.AppConstants;
import cn.zjiali.robot.util.PluginConfigUtil;
import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.factory.MessageFactory;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.model.response.JuHeBaseResponse;
import cn.zjiali.robot.model.response.YellowCalendarResponse;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.ObjectUtil;
import com.google.gson.reflect.TypeToken;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;


import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * 老黄历消息处理器
 *
 * @author zJiaLi
 * @since 2021-04-04 20:35
 */
public class YellowCalendarMessageEventHandler extends AbstractMessageEventHandler {

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String yellowCalendarMessage = MessageFactory.getYellowCalendarMessage(event.getMessage().contentToString());
        if (!ObjectUtil.isNullOrEmpty(yellowCalendarMessage)) {
            event.getGroup().sendMessage(yellowCalendarMessage);
        }
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        String yellowCalendarMessage = MessageFactory.getYellowCalendarMessage(event.getMessage().contentToString());
        if (!ObjectUtil.isNullOrEmpty(yellowCalendarMessage)) {
            event.getSender().sendMessage(yellowCalendarMessage);
        }
    }


    @Override
    public OutMessage handleGroupMessageEvent(GroupMessageEvent event) {
        return getYellowCalendarMessage();
    }

    @Override
    public OutMessage handleFriendMessageEvent(FriendMessageEvent event) {
        return getYellowCalendarMessage();
    }

    @Override
    public boolean next() {
        return false;
    }

    @Override
    public boolean matchCommand(String msg) {
        return containCommand(PluginCode.YELLOW_CALENDAR, msg);
    }

    /**
     * 获取老黄历消息
     *
     * @return
     */
    public OutMessage getYellowCalendarMessage() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        HashMap<String, Object> params = new HashMap<>();
        params.put("key", PluginConfigUtil.getApiKey(PluginCode.YELLOW_CALENDAR));
        params.put("date", date);
        String response = HttpUtil.get(PluginConfigUtil.getApiURL(PluginCode.YELLOW_CALENDAR), params);
        Type type = new TypeToken<JuHeBaseResponse<YellowCalendarResponse>>() {
        }.getType();
        JuHeBaseResponse<YellowCalendarResponse> baseResponse = JsonUtil.toObjByType(response, type);
        if (baseResponse.getError_code() == 0) {
            YellowCalendarResponse yellowCalendarResponse = baseResponse.getResult();
            return OutMessage.builder().convertFlag(true).templateCode(PluginCode.YELLOW_CALENDAR)
                    .pluginCode(PluginCode.YELLOW_CALENDAR)
                    .fillObj(yellowCalendarResponse).fillFlag(AppConstants.FILL_OUT_MESSAGE_OBJECT_FLAG).build();
        }
        return null;
    }

}
