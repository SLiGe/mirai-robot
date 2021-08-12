package cn.zjiali.robot.config.plugin;

import lombok.Data;

import java.util.HashMap;

/**
 * @author zJiaLi
 * @since 2021-08-12 16:53
 */
@Data
public class PluginConfig {

    private String pluginName;

    private String command;

    private int enable;

    private String template;

    /**
     * 消息处理器
     */
    private String handler;

    /**
     * 插件配置
     */
    private HashMap<String,String> properties;

}
