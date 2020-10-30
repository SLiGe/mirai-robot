package cn.zjiali.robot.handler;

import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;

/**
 * @author zJiaLi
 * @since 2020-10-29 20:57
 */
public class SignInHandler implements Handler{
    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        long id = event.getSender().getId();

    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {

    }
}
