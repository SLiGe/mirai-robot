package cn.zjiali.robot.config;

import cn.zjiali.robot.entity.ApplicationConfig;
import cn.zjiali.robot.handler.Handler;

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
    public static List<Handler> msgHandlers = new ArrayList<>();


    public static ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public static List<Handler> getMsgHandlers() {
        return msgHandlers;
    }
}
