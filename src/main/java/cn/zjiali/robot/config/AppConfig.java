package cn.zjiali.robot.config;

import cn.zjiali.robot.config.plugin.Plugin;
import cn.zjiali.robot.constant.PluginProperty;
import cn.zjiali.robot.model.ApplicationConfig;
import cn.zjiali.robot.handler.Handler;

import java.util.ArrayList;
import java.util.HashMap;
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



    public static String getQQ() {
        return getApplicationConfig().getQq();
    }

    public static List<Handler> getMsgHandlers() {
        return msgHandlers;
    }
}
