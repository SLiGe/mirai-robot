package cn.zjiali.robot.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.zjiali.robot.factory.HandlerFactory;
import cn.zjiali.robot.factory.ServiceFactory;
import cn.zjiali.robot.main.OutMessageConvert;
import cn.zjiali.robot.main.interceptor.HandlerInterceptor;
import cn.zjiali.robot.model.message.OutMessage;
import net.mamoe.mirai.event.events.AbstractMessageEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;

import java.util.List;

/**
 * 消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 21:20
 */
public class DefaultGlobalMessageHandler implements GlobalMessageHandler {

    public void handleGroupMessage(GroupMessageEvent event) {
        handleMessage(true, event, null);
    }

    public void handleFriendMessage(FriendMessageEvent event) {
        handleMessage(false, null, event);
    }

    private void handleMessage(boolean isGroup, GroupMessageEvent groupMessageEvent, FriendMessageEvent friendMessageEvent) {
        boolean preHandle = doPreHandle(isGroup ? groupMessageEvent : friendMessageEvent);
        if (!preHandle) return;
        String msg = isGroup ? groupMessageEvent.getMessage().contentToString() : friendMessageEvent.getMessage().contentToString();
        List<MessageEventHandler> messageEventHandlerList = HandlerFactory.getInstance().getBeanList(MessageEventHandler.class);
        for (MessageEventHandler messageEventHandler : messageEventHandlerList) {
            if (messageEventHandler.matchCommand(msg)) {
                if (isGroup) {
                    OutMessage outMessage = messageEventHandler.handleGroupMessageEvent(groupMessageEvent);
                    String message = OutMessageConvert.getInstance().convert(outMessage);
                    groupMessageEvent.getGroup().sendMessage(new At(groupMessageEvent.getSender().getId()).plus(message));
                } else {
                    OutMessage outMessage = messageEventHandler.handleFriendMessageEvent(friendMessageEvent);
                    String message = OutMessageConvert.getInstance().convert(outMessage);
                    friendMessageEvent.getSender().sendMessage(message);
                }
                if (!messageEventHandler.next()) break;
            }
        }
        triggerAfterCompletion(isGroup ? groupMessageEvent : friendMessageEvent);
    }

    /**
     * 调用前执行
     *
     * @param abstractMessageEvent 消息
     * @return 执行结果
     */
    public boolean doPreHandle(AbstractMessageEvent abstractMessageEvent) {
        boolean preHandle = false;
        List<HandlerInterceptor> handlerInterceptors = ServiceFactory.getInstance().getBeanList(HandlerInterceptor.class);
        if (CollectionUtil.isNotEmpty(handlerInterceptors)) {
            for (HandlerInterceptor handlerInterceptor : handlerInterceptors) {
                try {
                    preHandle = handlerInterceptor.preHandle(abstractMessageEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!preHandle) break;
            }
        }
        return preHandle;
    }


    /**
     * 完成后调用
     *
     * @param abstractMessageEvent 消息
     */
    public void triggerAfterCompletion(AbstractMessageEvent abstractMessageEvent) {
        List<HandlerInterceptor> handlerInterceptors = ServiceFactory.getInstance().getBeanList(HandlerInterceptor.class);
        if (CollectionUtil.isNotEmpty(handlerInterceptors)) {
            for (HandlerInterceptor handlerInterceptor : handlerInterceptors) {
                try {
                    handlerInterceptor.afterCompletion(abstractMessageEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
