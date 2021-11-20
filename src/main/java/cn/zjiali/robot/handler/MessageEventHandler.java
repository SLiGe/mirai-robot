package cn.zjiali.robot.handler;


import cn.zjiali.robot.model.message.OutMessage;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * @author zJiaLi
 * @since 2020-10-29 20:53
 */
public interface MessageEventHandler {

    /**
     * 处理群消息 -- 已过时,请参阅: {@link MessageEventHandler#handleGroupMessageEvent(GroupMessageEvent)}
     *
     * @param event 群消息事件
     */
    @Deprecated
    default void handleGroupMessage(GroupMessageEvent event) {
    }

    /**
     * 处理好友消息 -- 已过时,请参阅: {@link MessageEventHandler#handleFriendMessageEvent(FriendMessageEvent)}
     *
     * @param event 好友消息事件
     */
    @Deprecated
    default void handleFriendMessage(FriendMessageEvent event) {
    }

    /**
     * 处理群消息
     *
     * @param event 群消息事件
     */
    default OutMessage handleGroupMessageEvent(GroupMessageEvent event) {
        return null;
    }

    /**
     * 处理好友消息
     *
     * @param event 好友消息事件
     */
    default OutMessage handleFriendMessageEvent(FriendMessageEvent event) {
        return null;
    }

    /**
     * 是否继续下一个插件进行拦截
     *
     * @return boolean
     */
    default boolean next() {
        return true;
    }

    /**
     * 命令匹配是否匹配
     *
     * @return boolean
     */
    default boolean matchCommand(String msg) {
        return false;
    }

    /**
     * 排除关键词
     *
     * @param msg 消息
     * @return 是否忽略
     */
    default boolean ignore(String msg) {
        return false;
    }

}
