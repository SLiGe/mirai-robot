package cn.zjiali.robot.model;

import cn.zjiali.robot.config.plugin.Plugin;

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


    /*public static class Plugin {

        *//**
         * 插件名称
         *//*
        private String name;

        *//**
         * 是否启用 0 未启用 1 启用
         *//*
        private int enable;

        *//**
         * 消息处理器
         *//*
        private String handler;

        *//**
         * 配置类
         *//*
        private String configClass;

        public String getConfigClass() {
            return configClass;
        }

        public void setConfigClass(String configClass) {
            this.configClass = configClass;
        }

        *//**
         * 插件配置
         *//*
        private HashMap<String, String> properties;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getEnable() {
            return enable;
        }

        public void setEnable(int enable) {
            this.enable = enable;
        }

        public HashMap<String, String> getProperties() {
            return properties;
        }

        public void setProperties(HashMap<String, String> properties) {
            this.properties = properties;
        }

        public String getHandler() {
            return handler;
        }

        public void setHandler(String handler) {
            this.handler = handler;
        }
    }
*/
    public int getAppEnable() {
        return appEnable;
    }

    public void setAppEnable(int appEnable) {
        this.appEnable = appEnable;
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<Plugin> plugins) {
        this.plugins = plugins;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
