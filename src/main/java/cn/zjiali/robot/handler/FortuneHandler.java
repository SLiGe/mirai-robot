package cn.zjiali.robot.handler;

import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;

/**
 * 运势消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 20:58
 */
public class FortuneHandler implements Handler{

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String msgString = event.getMessage().contentToString();
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {

    }
}
