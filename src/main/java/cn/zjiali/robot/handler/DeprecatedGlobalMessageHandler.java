package cn.zjiali.robot.handler;

import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.config.Plugin;
import cn.zjiali.robot.factory.MessageEventHandlerFactory;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 该处理器已过时
 * 替换 -> {@link DefaultGlobalMessageHandler}
 * @author zJiaLi
 * @since 2021-09-04 19:04
 */
@Deprecated
public class DeprecatedGlobalMessageHandler implements GlobalMessageHandler {

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        handleMessage(true, event, null);
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        handleMessage(false, null, event);
    }

    private void handleMessage(boolean isGroup, GroupMessageEvent groupMessageEvent, FriendMessageEvent friendMessageEvent) {
        boolean preHandle = doPreHandle(isGroup ? groupMessageEvent : friendMessageEvent);
        if (!preHandle) return;
        // 茉莉插件需要单独拦截
        String msg = isGroup ? groupMessageEvent.getMessage().contentToString() : friendMessageEvent.getMessage().contentToString();
        List<Plugin> plugins = AppConfig.getApplicationConfig().getPlugins();
        plugins.stream().filter(plugin -> "茉莉聊天".equals(plugin.getName()) && plugin.getEnable() == 1)
                .findFirst().ifPresent((plugin) -> {
                    MessageEventHandler handler = MessageEventHandlerFactory.getInstance().get(plugin.getName());
                    if (isGroup) {
                        handler.handleGroupMessage(groupMessageEvent);
                    } else {
                        handler.handleFriendMessage(friendMessageEvent);
                    }
                });
        plugins.stream().filter(plugin -> !"茉莉聊天".equals(plugin.getName())).forEach(
                plugin -> {
                    HashMap<String, String> pluginProperties = plugin.getProperties();
                    int enable = plugin.getEnable();
                    String pluginName = plugin.getName();
                    String command = pluginProperties.get("command");
                    List<String> commandArray = Arrays.asList(command.split(","));
                    if (enable == 1 && containCommand(msg, commandArray)) {
                        MessageEventHandler handler = MessageEventHandlerFactory.getInstance().get(pluginName);
                        if (isGroup) {
                            handler.handleGroupMessage(groupMessageEvent);
                        } else {
                            handler.handleFriendMessage(friendMessageEvent);
                        }
                    }
                }
        );
        triggerAfterCompletion(isGroup ? groupMessageEvent : friendMessageEvent, null);
    }


    private static boolean containCommand(String msg, List<String> commandArray) {
        for (String command : commandArray) {
            if (msg.contains(command))
                return true;
        }
        return false;
    }
}
