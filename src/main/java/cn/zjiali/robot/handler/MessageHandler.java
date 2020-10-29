package cn.zjiali.robot.handler;

import cn.zjiali.robot.config.AppConfig;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;

import java.util.List;

/**
 * 消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 21:20
 */
public class MessageHandler {

    public static void handleGroupMessage(GroupMessageEvent event) {
        List<Handler> msgHandlers = AppConfig.getMsgHandlers();
        if (msgHandlers != null && msgHandlers.size() > 0) {
            msgHandlers.forEach(handler -> handler.handleGroupMessage(event));
        }
    }

    public static void handleFriendMessage(FriendMessageEvent event) {
        List<Handler> msgHandlers = AppConfig.getMsgHandlers();
        if (msgHandlers != null && msgHandlers.size() > 0) {
            msgHandlers.forEach(handler -> handler.handleFriendMessage(event));
        }
    }
}
