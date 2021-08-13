package cn.zjiali.robot.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.config.plugin.Plugin;
import cn.zjiali.robot.model.ApplicationConfig;
import cn.zjiali.robot.factory.HandlerFactory;
import cn.zjiali.robot.factory.ServiceFactory;
import cn.zjiali.robot.main.interceptor.HandlerInterceptor;
import net.mamoe.mirai.event.events.AbstractMessageEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.*;

/**
 * 消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 21:20
 */
public class GlobalMessageHandler {

    public static void handleGroupMessage(GroupMessageEvent event) {
        handleMessage(true, event, null);
    }

    public static void handleFriendMessage(FriendMessageEvent event) {
        handleMessage(false, null, event);
    }

    private static void handleMessage(boolean isGroup, GroupMessageEvent groupMessageEvent, FriendMessageEvent friendMessageEvent) {
        boolean preHandle = doPreHandle(isGroup ? groupMessageEvent : friendMessageEvent);
        if (!preHandle) return;
        // 茉莉插件需要单独拦截
        String msg = isGroup ? groupMessageEvent.getMessage().contentToString() : friendMessageEvent.getMessage().contentToString();
        List<Plugin> plugins = AppConfig.getApplicationConfig().getPlugins();
        plugins.stream().filter(plugin -> "茉莉聊天".equals(plugin.getName()) && plugin.getEnable() == 1)
                .findFirst().ifPresent((plugin) -> {
                    Handler handler = HandlerFactory.getInstance().get(plugin.getName());
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
                        Handler handler = HandlerFactory.getInstance().get(pluginName);
                        if (isGroup) {
                            handler.handleGroupMessage(groupMessageEvent);
                        } else {
                            handler.handleFriendMessage(friendMessageEvent);
                        }
                    }
                }
        );
        triggerAfterCompletion(isGroup ? groupMessageEvent : friendMessageEvent);
    }

    /**
     * 调用前执行
     *
     * @param abstractMessageEvent 消息
     * @return 执行结果
     */
    private static boolean doPreHandle(AbstractMessageEvent abstractMessageEvent) {
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
    private static void triggerAfterCompletion(AbstractMessageEvent abstractMessageEvent) {
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


    private static boolean containCommand(String msg, List<String> commandArray) {
        for (String command : commandArray) {
            if (msg.contains(command))
                return true;
        }
        return false;
    }
}
