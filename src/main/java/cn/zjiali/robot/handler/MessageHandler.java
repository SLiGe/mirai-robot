package cn.zjiali.robot.handler;

import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.entity.ApplicationConfig;
import cn.zjiali.robot.factory.HandlerFactory;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 21:20
 */
public class MessageHandler {

    public static void handleGroupMessage(GroupMessageEvent event) {
        handleMessage(true, event, null);
    }

    public static void handleFriendMessage(FriendMessageEvent event) {
        handleMessage(false, null, event);
    }

    private static void handleMessage(boolean isGroup, GroupMessageEvent groupMessageEvent, FriendMessageEvent friendMessageEvent) {
        String msg = isGroup ? groupMessageEvent.getMessage().contentToString() : friendMessageEvent.getMessage().contentToString();
        List<ApplicationConfig.Plugin> plugins = AppConfig.getApplicationConfig().getPlugins();
        for (ApplicationConfig.Plugin plugin : plugins) {
            HashMap<String, String> pluginProperties = plugin.getProperties();
            int enable = plugin.getEnable();
            String pluginName = plugin.getName();
            String command = pluginProperties.get("command");
            List<String> commandArray = Arrays.asList(command.split(","));
            if (enable == 1 && containCommand(msg, commandArray)) {
                Handler handler = HandlerFactory.get(pluginName);
                if (isGroup) {
                    handler.handleGroupMessage(groupMessageEvent);
                } else {
                    handler.handleFriendMessage(friendMessageEvent);
                }
            }
        }
    }

    private static boolean containCommand(String msg, List<String> commandArray) {
        for (String command : commandArray) {
            if (msg.contains(command))
                return true;
        }
        return false;
    }
}
