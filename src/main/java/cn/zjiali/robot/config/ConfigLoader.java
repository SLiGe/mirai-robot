package cn.zjiali.robot.config;

import cn.zjiali.robot.entity.ApplicationConfig;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author zJiaLi
 * @since 2020-10-29 20:31
 */
public class ConfigLoader {

    public static void loadAppConfig() throws IOException {
        InputStream appStream = ConfigLoader.class.getResourceAsStream("/application.properties");
        Properties appProfileProperties = new Properties();
        appProfileProperties.load(appStream);
        String appProfile = appProfileProperties.getProperty("application.profile");
        InputStream configStream = ConfigLoader.class.getResourceAsStream("/application-" + appProfile + ".json");
        String appConfigJson = new BufferedReader(new InputStreamReader(configStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        AppConfig.applicationConfig = new Gson().fromJson(appConfigJson, ApplicationConfig.class);
        configStream.close();
        appStream.close();
    }

    public static void main(String[] args) {
        try {
            loadAppConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
