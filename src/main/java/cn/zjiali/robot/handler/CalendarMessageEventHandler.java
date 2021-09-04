package cn.zjiali.robot.handler;

import cn.zjiali.robot.config.plugin.PluginConfig;
import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.factory.MessageFactory;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.model.response.CalendarResponse;
import cn.zjiali.robot.model.response.JuHeBaseResponse;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.ObjectUtil;
import com.google.gson.reflect.TypeToken;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 万年历处理器
 *
 * @author zJiaLi
 * @since 2021-04-04 11:02
 */
public class CalendarMessageEventHandler extends AbstractMessageEventHandler {
    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String calendarMessage = MessageFactory.getCalendarMessage(event.getMessage().contentToString());
        if (!ObjectUtil.isNullOrEmpty(calendarMessage)) {
            event.getGroup().sendMessage(calendarMessage);
        }
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        String calendarMessage = MessageFactory.getCalendarMessage(event.getMessage().contentToString());
        if (!ObjectUtil.isNullOrEmpty(calendarMessage)) {
            event.getSender().sendMessage(calendarMessage);
        }
    }

    @Override
    public OutMessage handleGroupMessageEvent(GroupMessageEvent event) {
        return getCalendarMessage();
    }

    @Override
    public OutMessage handleFriendMessageEvent(FriendMessageEvent event) {
        return getCalendarMessage();
    }

    @Override
    public boolean next() {
        return false;
    }

    @Override
    public boolean matchCommand(String msg) {
        return containCommand(PluginCode.CALENDAR,msg);
    }

    /**
     * 获取万年历消息
     *
     * @return
     */
    public static OutMessage getCalendarMessage() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateStr = year + "-" + month + "-" + day;
        HashMap<String, Object> params = new HashMap<>();
        params.put("key", PluginConfig.getApiKey(PluginCode.CALENDAR));
        params.put("date", dateStr);
        String response = HttpUtil.get(PluginConfig.getApiURL(PluginCode.CALENDAR), params);
        Type type = new TypeToken<JuHeBaseResponse<CalendarResponse>>() {
        }.getType();
        JuHeBaseResponse<CalendarResponse> baseResponse = JsonUtil.toObjByType(response, type);
        if (baseResponse.getError_code() == 0) {
            CalendarResponse calendarResponse = baseResponse.getResult();
            return OutMessage.builder().convertFlag(true).fillFlag(2).fillObj(calendarResponse).templateCode(PluginCode.CALENDAR).build();
        }
        return null;
    }

}
