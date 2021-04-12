package cn.zjiali.robot.handler;

import cn.zjiali.robot.util.ObjectUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;


import static cn.zjiali.robot.factory.MessageFactory.getCalendarMessage;

/**
 * 万年历处理器
 *
 * @author zJiaLi
 * @since 2021-04-04 11:02
 */
public class CalendarHandler implements Handler {
    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String calendarMessage = getCalendarMessage(event.getMessage().contentToString());
        if (!ObjectUtil.isNullOrEmpty(calendarMessage)) {
            event.getGroup().sendMessage(calendarMessage);
        }
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        String calendarMessage = getCalendarMessage(event.getMessage().contentToString());
        if (!ObjectUtil.isNullOrEmpty(calendarMessage)) {
            event.getSender().sendMessage(calendarMessage);
        }
    }

}
