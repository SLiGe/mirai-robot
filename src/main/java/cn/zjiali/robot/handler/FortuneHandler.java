package cn.zjiali.robot.handler;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;

import static cn.zjiali.robot.factory.MessageFactory.getFortuneMsg;

/**
 * 运势消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 20:58
 */
public class FortuneHandler implements Handler {

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String msgString = event.getMessage().contentToString();
        long qq = event.getSender().getId();
        long groupNum = event.getGroup().getId();
        if ("运势".equals(msgString)) {
            String fortuneMsg = getFortuneMsg(qq, groupNum, 2);
            if (fortuneMsg != null) {
                event.getGroup().sendMessage(new At(event.getSender().getId()).plus(fortuneMsg));
            }
        }
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        String msgString = event.getMessage().contentToString();
        long qq = event.getSender().getId();
        if ("运势".equals(msgString)) {
            String fortuneMsg = getFortuneMsg(qq, 0, 1);
            if (fortuneMsg != null) {
                event.getSender().sendMessage(fortuneMsg);
            }
        }
    }


}
