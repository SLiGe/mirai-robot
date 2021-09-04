package cn.zjiali.robot.handler;

import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.model.message.OutMessage;
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
public class TodayOfHistoryMessageEventHandler extends AbstractMessageEventHandler {

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String msg = event.getMessage().contentToString();
        String todayOnHistoryMessage = getTodayOnHistoryMessage();
        if (!ObjectUtil.isNullOrEmpty(todayOnHistoryMessage)) {
            event.getGroup().sendMessage(msg);
        }
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        String msg = event.getMessage().contentToString();
        String todayOnHistoryMessage = getTodayOnHistoryMessage();
        if (!ObjectUtil.isNullOrEmpty(todayOnHistoryMessage)) {
            event.getSender().sendMessage(msg);
        }
    }


    @Override
    public OutMessage handleGroupMessageEvent(GroupMessageEvent event) {
        return OutMessage.builder().convertFlag(false).content(getTodayOnHistoryMessage()).build();
    }

    @Override
    public OutMessage handleFriendMessageEvent(FriendMessageEvent event) {
        return OutMessage.builder().convertFlag(false).content(getTodayOnHistoryMessage()).build();
    }

    @Override
    public boolean next() {
        return false;
    }

    @Override
    public boolean matchCommand(String msg) {
        return containCommand(PluginCode.TODAY_HISTORY,msg);
    }
}
