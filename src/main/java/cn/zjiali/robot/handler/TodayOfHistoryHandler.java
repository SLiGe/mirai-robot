package cn.zjiali.robot.handler;

import cn.zjiali.robot.util.ObjectUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;


import static cn.zjiali.robot.factory.MessageFactory.getTodayOnHistoryMessage;

/**
 * 历史上的今天
 *
 * @author zJiaLi
 * @since 2021-03-21 11:33
 */
public class TodayOfHistoryHandler implements Handler {

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String msg = event.getMessage().contentToString();
        String todayOnHistoryMessage = getTodayOnHistoryMessage(event.getMessage().contentToString());
        if (!ObjectUtil.isNullOrEmpty(todayOnHistoryMessage)) {
            event.getGroup().sendMessage(msg);
        }
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        String msg = event.getMessage().contentToString();
        String todayOnHistoryMessage = getTodayOnHistoryMessage(event.getMessage().contentToString());
        if (!ObjectUtil.isNullOrEmpty(todayOnHistoryMessage)) {
            event.getSender().sendMessage(msg);
        }
    }




}
