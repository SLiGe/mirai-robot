package cn.zjiali.robot.config;

import cn.zjiali.robot.handler.MessageEventHandler;
import cn.zjiali.robot.model.ApplicationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zJiaLi
 * @since 2020-10-29 20:41
 */
public class AppConfig {

    /**
     * 应用配置
     */
    public static ApplicationConfig applicationConfig;

    /**
     * 消息处理器
     */
    public static List<MessageEventHandler> msgMessageEventHandlers = new ArrayList<>();


    public static ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }


    public static String getQQ() {
        return getApplicationConfig().getQq();
    }

    public static Long qq() {
        return Long.parseLong(getApplicationConfig().getQq());
    }

    public static List<MessageEventHandler> getMsgHandlers() {
        return msgMessageEventHandlers;
    }

    public static boolean serverControl() {
        return applicationConfig.serverControl();
    }
}
