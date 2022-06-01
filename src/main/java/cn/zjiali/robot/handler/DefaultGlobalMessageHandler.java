package cn.zjiali.robot.handler;

import cn.zjiali.robot.constant.AppConstants;
import cn.zjiali.robot.main.OutMessageConvert;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.At;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * 消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 21:20
 */
public class DefaultGlobalMessageHandler implements GlobalMessageHandler {

    @Inject
    private Set<MessageEventHandler> messageEventHandlers;

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
        //处理器排序
        ArrayList<MessageEventHandler> messageEventHandlerList = Lists.newArrayList(messageEventHandlers);
        messageEventHandlerList.sort(Comparator.comparingInt(MessageEventHandler::order));
        List<OutMessage> outMessageList = Lists.newLinkedList();
        for (MessageEventHandler messageEventHandler : messageEventHandlerList) {
            if (messageEventHandler.matchCommand(msg) && !messageEventHandler.ignore(msg)) {
                OutMessage outMessage;
                if (isGroup) {
                    outMessage = messageEventHandler.handleGroupMessageEvent(groupMessageEvent);
                    if (outMessage == null) continue;
                    int messageType = outMessage.getMessageType();
                    if (messageType == AppConstants.MESSAGE_TYPE_HANDLER) {
                        String message = OutMessageConvert.getInstance().convert(outMessage);
                        if (!ObjectUtil.isNullOrEmpty(message))
                            groupMessageEvent.getGroup().sendMessage(new At(groupMessageEvent.getSender().getId()).plus(message));
                    } else if (messageType == AppConstants.MESSAGE_TYPE_PLUGIN) {
                        if (outMessage.getMessage() != null) {
                            groupMessageEvent.getGroup().sendMessage(outMessage.getMessage());
                        }
                    }
                } else {
                    outMessage = messageEventHandler.handleFriendMessageEvent((FriendMessageEvent) friendMessageEvent);
                    if (outMessage == null) continue;
                    int messageType = outMessage.getMessageType();
                    if (messageType == AppConstants.MESSAGE_TYPE_HANDLER) {
                        String message = OutMessageConvert.getInstance().convert(outMessage);
                        if (!ObjectUtil.isNullOrEmpty(message)) friendMessageEvent.getSender().sendMessage(message);
                    } else if (messageType == AppConstants.MESSAGE_TYPE_PLUGIN) {
                        if (outMessage.getMessage() != null) {
                            friendMessageEvent.getSender().sendMessage(outMessage.getMessage());
                        }
                    }
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
