package cn.zjiali.robot.handler;

import cn.zjiali.robot.factory.MessageEventHandlerFactory;
import cn.zjiali.robot.main.OutMessageConvert;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.util.ObjectUtil;
import com.google.common.collect.Lists;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.At;

import java.util.List;

/**
 * 消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 21:20
 */
public class DefaultGlobalMessageHandler implements GlobalMessageHandler {

    public void handleGroupMessageEvent(GroupMessageEvent event) {
        handleMessage(true, event, null);
    }

    public void handleFriendMessageEvent(FriendMessageEvent event) {
        handleMessage(false, null, event);
    }

    @Override
    public void handleOtherMessageEvent(MessageEvent event) {
        handleMessage(false, null, event);
    }

    private void handleMessage(boolean isGroup, GroupMessageEvent groupMessageEvent, MessageEvent friendMessageEvent) {
        boolean preHandle = doPreHandle(isGroup ? groupMessageEvent : friendMessageEvent);
        if (!preHandle) return;
        String msg = isGroup ? groupMessageEvent.getMessage().contentToString() : friendMessageEvent.getMessage().contentToString();
        List<MessageEventHandler> messageEventHandlerList = MessageEventHandlerFactory.getInstance().getBeanList(MessageEventHandler.class);
        List<OutMessage> outMessageList = Lists.newLinkedList();
        for (MessageEventHandler messageEventHandler : messageEventHandlerList) {
            if (messageEventHandler.matchCommand(msg) && !messageEventHandler.ignore(msg)) {
                OutMessage outMessage;
                if (isGroup) {
                    outMessage = messageEventHandler.handleGroupMessageEvent(groupMessageEvent);
                    String message = OutMessageConvert.getInstance().convert(outMessage);
                    if (!ObjectUtil.isNullOrEmpty(message))
                        groupMessageEvent.getGroup().sendMessage(new At(groupMessageEvent.getSender().getId()).plus(message));
                } else {
                    outMessage = messageEventHandler.handleFriendMessageEvent((FriendMessageEvent) friendMessageEvent);
                    String message = OutMessageConvert.getInstance().convert(outMessage);
                    if (!ObjectUtil.isNullOrEmpty(message)) friendMessageEvent.getSender().sendMessage(message);
                }
                if (ObjectUtil.isNotNullOrEmpty(outMessage)) {
                    outMessageList.add(outMessage);
                }
                if (!messageEventHandler.next()) break;
            }
        }
        triggerAfterCompletion(isGroup ? groupMessageEvent : friendMessageEvent, outMessageList);
    }

}
