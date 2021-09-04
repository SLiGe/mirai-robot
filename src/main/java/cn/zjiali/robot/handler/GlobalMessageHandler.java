package cn.zjiali.robot.handler;

import net.mamoe.mirai.event.events.AbstractMessageEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * 消息事件总处理器
 * * <p>
 * * default see {@link DefaultGlobalMessageHandler}
 * * </p>
 *
 * @author zJiaLi
 * @since 2021-09-04 15:15
 */
public interface GlobalMessageHandler {

    /**
     * 消息事件前置处理
     *
     * @param abstractMessageEvent 消息事件
     * @return boolean
     */
    boolean doPreHandle(AbstractMessageEvent abstractMessageEvent);

    /**
     * 处理群组消息
     *
     * @param event 群组消息事件
     */
    void handleGroupMessage(GroupMessageEvent event);

    /**
     * 处理好友消息
     *
     * @param event 好友消息事件
     */
    void handleFriendMessage(FriendMessageEvent event);

    /**
     * 消息事件后置处理
     *
     * @param abstractMessageEvent 消息事件
     */
    void triggerAfterCompletion(AbstractMessageEvent abstractMessageEvent);
}
