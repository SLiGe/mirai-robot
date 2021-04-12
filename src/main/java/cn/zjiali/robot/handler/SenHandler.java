package cn.zjiali.robot.handler;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import static cn.zjiali.robot.factory.MessageFactory.getSen;

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


}
