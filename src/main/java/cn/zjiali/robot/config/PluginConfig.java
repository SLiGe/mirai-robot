package cn.zjiali.robot.config;

import cn.zjiali.robot.entity.ApplicationConfig;

import java.util.HashMap;
import java.util.List;

/**
 * @author zJiaLi
 * @since 2020-10-29 20:50
 */
public class PluginConfig {

    public static String sen_command;

    public static int sen_enable;

    public static String sign_in_command;

    public static int sign_in_enable;

    public static String sign_in_cur_level;

    public static String sign_in_day_sen;

    public static String fortune_command;

    public static int fortune_enable;

    public static String fortune_sign_text;

    public static String fortune_star_num;

    public static String fortune_un_sign;

    public static String fortune_day_one;

    public static String fortune_point;

    static {
        ApplicationConfig applicationConfig = AppConfig.getApplicationConfig();
        int appEnable = applicationConfig.getAppEnable();
        if (appEnable == 1) {
            List<ApplicationConfig.Plugin> plugins = applicationConfig.getPlugins();
            for (ApplicationConfig.Plugin plugin : plugins) {
                String pluginName = plugin.getName();
                HashMap<String, String> pluginProperties = plugin.getProperties();
                if ("一言".equals(pluginName)) {
                    sen_command = pluginProperties.getOrDefault("command", "一言");
                    sen_enable = plugin.getEnable();
                } else if ("签到".equals(pluginName)) {
                    sign_in_command = pluginProperties.getOrDefault("command", "签到");
                    sign_in_enable = plugin.getEnable();
                    sign_in_cur_level = pluginProperties.getOrDefault("cur_level", "1");
                    sign_in_day_sen = pluginProperties.getOrDefault("day_sen", "1");
                } else if ("今日运势".equals(pluginName)) {
                    fortune_command = pluginProperties.getOrDefault("command", "运势");
                    fortune_day_one = pluginProperties.getOrDefault("day_one", "1");
                    fortune_enable = plugin.getEnable();
                    fortune_point = pluginProperties.getOrDefault("point", "1");
                    fortune_sign_text = pluginProperties.getOrDefault("sign_text", "1");
                    fortune_star_num = pluginProperties.getOrDefault("star_num", "1");
                    fortune_un_sign = pluginProperties.getOrDefault("un_sign", "1");
                }
            }
        }
    }
}
