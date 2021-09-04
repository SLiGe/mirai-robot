package cn.zjiali.robot.handler;

import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.model.message.OutMessage;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import static cn.zjiali.robot.factory.MessageFactory.getSen;

/**
 * 一言处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 20:57
 */
public class SenMessageEventHandler extends AbstractMessageEventHandler {

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        event.getGroup().sendMessage(getSen());
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        event.getFriend().sendMessage(getSen());
    }


    @Override
    public OutMessage handleGroupMessageEvent(GroupMessageEvent event) {
        return OutMessage.builder().convertFlag(false).content(getSen()).build();
    }

    @Override
    public OutMessage handleFriendMessageEvent(FriendMessageEvent event) {
        return OutMessage.builder().convertFlag(false).content(getSen()).build();
    }

    @Override
    public boolean next() {
        return true;
    }

    @Override
    public boolean matchCommand(String msg) {
        return containCommand(PluginCode.ONE_SEN, msg);
    }
}
