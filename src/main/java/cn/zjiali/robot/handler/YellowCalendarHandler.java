package cn.zjiali.robot.handler;

import cn.zjiali.robot.util.ObjectUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;


import static cn.zjiali.robot.factory.MessageFactory.getYellowCalendarMessage;

/**
 * 老黄历消息处理器
 *
 * @author zJiaLi
 * @since 2021-04-04 20:35
 */
public class YellowCalendarHandler implements Handler {

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String yellowCalendarMessage = getYellowCalendarMessage(event.getMessage().contentToString());
        if (!ObjectUtil.isNullOrEmpty(yellowCalendarMessage)){
            event.getGroup().sendMessage(yellowCalendarMessage);
        }
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        String yellowCalendarMessage = getYellowCalendarMessage(event.getMessage().contentToString());
        if (!ObjectUtil.isNullOrEmpty(yellowCalendarMessage)){
            event.getSender().sendMessage(yellowCalendarMessage);
        }
    }


}
