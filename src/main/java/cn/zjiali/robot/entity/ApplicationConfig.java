package cn.zjiali.robot.entity;

import java.util.HashMap;
import java.util.List;

/**
 * @author zJiaLi
 * @since 2020-10-29 16:13
 */
public class ApplicationConfig {

    /**
     * 是否启用总控制
     */
    private int appEnable;

    private List<Plugin> plugins;

    private String qq;

    private String password;


    public static class Plugin {

        /**
         * 插件名称
         */
        private String name;

        /**
         * 是否启用 0 未启用 1 启用
         */
        private int enable;

        /**
         * 插件配置
         */
        private HashMap<String, String> properties;
    }


}
