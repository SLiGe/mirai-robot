package cn.zjiali.robot.config.plugin;

import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.constant.PluginProperty;

import java.util.HashMap;

/**
 * @author zJiaLi
 * @since 2021-08-13 14:11
 */
public class PluginConfig {

    public static String getConfigVal(String pluginCode, String key) {
        Plugin pluginI = getPlugin(pluginCode);
        HashMap<String, String> properties = pluginI.getProperties();
        return properties.get(key);
    }

    public static Plugin getPlugin(String pluginCode){
        return AppConfig.getApplicationConfig().getPlugins().stream().filter(plugin -> pluginCode.equals(plugin.getCode()))
                .findFirst().orElseThrow(() -> new NullPointerException("未找到插件!"));
    }

    public static String getTemplate(String pluginCode){
        return getPlugin(pluginCode).getTemplate();
    }

    public static String getApiKey(String pluginCode){
        return getConfigVal(pluginCode, PluginProperty.API_KEY);
    }

    public static String getApiSecret(String pluginCode){
        return getConfigVal(pluginCode, PluginProperty.API_SECRET);
    }

    public static String getApiURL(String pluginCode){
        return getConfigVal(pluginCode, PluginProperty.API_URL);
    }

    public static String getCommand(String pluginCode){
        return getPlugin(pluginCode).getCommand();
    }

}
