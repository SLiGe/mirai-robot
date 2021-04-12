package cn.zjiali.robot.handler;


import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * @author zJiaLi
 * @since 2020-10-29 20:53
 */
public interface Handler {

    /**
     * 处理群消息
     *
     * @param event 群消息事件
     */
    void handleGroupMessage(GroupMessageEvent event);

    /**
     * 处理好友消息
     *
     * @param event 好友消息事件
     */
    void handleFriendMessage(FriendMessageEvent event);

}
