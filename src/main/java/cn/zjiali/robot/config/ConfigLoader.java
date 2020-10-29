package cn.zjiali.robot.config;

import cn.zjiali.robot.entity.ApplicationConfig;
import cn.zjiali.robot.handler.Handler;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author zJiaLi
 * @since 2020-10-29 20:31
 */
public class ConfigLoader {


    public static void loadAppConfig() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        InputStream appStream = ConfigLoader.class.getResourceAsStream("/application.properties");
        Properties appProfileProperties = new Properties();
        appProfileProperties.load(appStream);
        String appProfile = appProfileProperties.getProperty("application.profile");
        InputStream configStream = ConfigLoader.class.getResourceAsStream("/application-" + appProfile + ".json");
        String appConfigJson = new BufferedReader(new InputStreamReader(configStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        final ApplicationConfig applicationConfig = new Gson().fromJson(appConfigJson, ApplicationConfig.class);
        AppConfig.applicationConfig = applicationConfig;
        final List<ApplicationConfig.Plugin> plugins = applicationConfig.getPlugins();
        if (plugins != null) {
            for (ApplicationConfig.Plugin plugin : plugins) {
                String pluginName = plugin.getName();
                String pluginHandler = plugin.getHandler();
                int pluginEnable = plugin.getEnable();
                if (pluginEnable == 1) {
                    Class<?> aClass = Class.forName(pluginHandler);
                    Object instance = aClass.getDeclaredConstructor().newInstance();
                    if (instance instanceof Handler) {
                        AppConfig.msgHandlers.add((Handler) instance);
                        System.out.println("[loadAppConfig]====加载 " + pluginName + " 成功！");
                    }
                }
            }
        }
        configStream.close();
        appStream.close();
    }

    public static void main(String[] args) {
        try {
            loadAppConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
