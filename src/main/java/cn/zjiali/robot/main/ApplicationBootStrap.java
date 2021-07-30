package cn.zjiali.robot.main;

import cn.zjiali.robot.RobotApplication;
import cn.zjiali.robot.annotation.Application;
import cn.zjiali.robot.annotation.Property;
import cn.zjiali.robot.annotation.Service;
import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.constant.ServerUrl;
import cn.zjiali.robot.model.ApplicationConfig;
import cn.zjiali.robot.factory.HandlerFactory;
import cn.zjiali.robot.factory.ServiceFactory;
import cn.zjiali.robot.handler.Handler;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.ObjectUtil;
import cn.zjiali.robot.util.PackageUtil;
import cn.zjiali.robot.util.PropertiesUtil;
import net.mamoe.mirai.utils.MiraiLogger;
import net.mamoe.mirai.utils.PlatformLogger;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 启动引导
 *
 * @author zJiaLi
 * @since 2021-04-07 21:37
 */
public class ApplicationBootStrap {

    private static final MiraiLogger miraiLogger = new PlatformLogger(ApplicationBootStrap.class.getName());

    private ApplicationBootStrap() {
    }

    private static final ApplicationBootStrap applicationBootStrap = new ApplicationBootStrap();

    public static ApplicationBootStrap getInstance() {
        return applicationBootStrap;
    }

    public void init() throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        loadService();
        loadAppConfig();
        loadServerUrl();
        loadPluginConfig();
    }

    /**
     * 加载业务类
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void loadService() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Application annotation = RobotApplication.class.getAnnotation(Application.class);
        String[] basePackages = annotation.basePackages();
        for (String basePackage : basePackages) {
            List<String> className = PackageUtil.getClassName(basePackage, true);
            if (!ObjectUtil.isNullOrEmpty(className)) {
                for (String clz : className) {
                    Class<?> aClass = Class.forName(clz, false, this.getClass().getClassLoader());
                    Service service = aClass.getAnnotation(Service.class);
                    if (service != null) {
                        Object instance = aClass.newInstance();
                        String serviceName = "".equals(service.name()) ? instance.getClass().getSimpleName() : service.name();
                        ServiceFactory.getInstance().put(serviceName, instance);
                    }
                }
            }
        }
    }


    /**
     * 加载应用配置
     *
     * @throws IOException
     */
    private void loadAppConfig() throws IOException {
        InputStream configStream = null;
        // 是否启用本地配置文件
        String localConfigFileFlag = PropertiesUtil.getProperty("application.properties", "application.config.file.local");
        if ("true".equals(localConfigFileFlag)) {
            String configFilePath = System.getProperty("application.config.file");
            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                configStream = new FileInputStream(configFile);
            }
        } else {
            String appProfile = PropertiesUtil.getProperty("application.properties", "application.profile");
            configStream = ApplicationBootStrap.class.getResourceAsStream("/application-" + appProfile + ".json");
        }
        if (configStream == null) {
            miraiLogger.error("[loadAppConfig]====加载配置文件失败,自动退出!");
            System.exit(0);
        }
        String appConfigJson = new BufferedReader(new InputStreamReader(configStream, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        final ApplicationConfig applicationConfig = JsonUtil.json2obj(appConfigJson, ApplicationConfig.class);
        AppConfig.applicationConfig = applicationConfig;
        final List<ApplicationConfig.Plugin> plugins = applicationConfig.getPlugins();
        if (plugins != null) {
            for (ApplicationConfig.Plugin plugin : plugins) {
                String pluginName = plugin.getName();
                String pluginHandler = plugin.getHandler();
                int pluginEnable = plugin.getEnable();
                if (pluginEnable == 1) {
                    Handler handler = HandlerFactory.getInstance().put(pluginName, pluginHandler);
                    AppConfig.msgHandlers.add(handler);
                    miraiLogger.info("[loadAppConfig]====加载 " + pluginName + " 成功！");
                }
            }
        }
        configStream.close();
    }

    /**
     * 加载接口地址
     *
     * @throws IOException
     * @throws IllegalAccessException
     */
    private void loadServerUrl() throws IOException, IllegalAccessException {
        Class<ServerUrl> serverUrlClass = ServerUrl.class;
        Field[] declaredFields = serverUrlClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Property property = declaredField.getAnnotation(Property.class);
            if (property != null) {
                String name = property.name();
                String value = property.value();
                declaredField.setAccessible(true);
                String apiValue = PropertiesUtil.getProperty("api.properties", name);
                if ("".equals(value)) {
                    declaredField.set(null, apiValue);
                } else {
                    declaredField.set(null, value);
                }
                miraiLogger.info("[loadServerUrl]====name: " + name + " value: " + ("".equals(value) ? apiValue : value));
            }
        }

    }

    /**
     * 加载插件配置
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    private void loadPluginConfig() throws ClassNotFoundException, IllegalAccessException {
        ApplicationConfig applicationConfig = AppConfig.getApplicationConfig();
        int appEnable = applicationConfig.getAppEnable();
        if (appEnable == 1) {
            List<ApplicationConfig.Plugin> plugins = applicationConfig.getPlugins();
            for (ApplicationConfig.Plugin plugin : plugins) {
                String pluginName = plugin.getName();
                String configClass = plugin.getConfigClass();
                HashMap<String, String> pluginProperties = plugin.getProperties();
                if (!"".equals(configClass)) {
                    Class<?> pluginConfigClass = Class.forName(configClass);
                    Field[] declaredFields = pluginConfigClass.getDeclaredFields();
                    for (Field declaredField : declaredFields) {
                        Property property = declaredField.getAnnotation(Property.class);
                        if (property != null) {
                            String propertyName = property.name();
                            declaredField.setAccessible(true);
                            if ("enable".equals(propertyName)) {
                                declaredField.set(null, plugin.getEnable());
                                miraiLogger.info("[loadPluginConfig]====插件:" + pluginName + " configName: " + propertyName + " configValue: " + plugin.getEnable());
                                continue;
                            }
                            String configValue = pluginProperties.get(propertyName);
                            String propertyValue = property.value();
                            if ("".equals(propertyValue)) {
                                declaredField.set(null, configValue);
                            } else {
                                declaredField.set(null, propertyValue);
                            }
                            miraiLogger.info("[loadPluginConfig]====插件:" + pluginName + " configName: " + propertyName + " configValue: " + ("".equals(propertyValue) ? configValue : propertyValue));
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            getInstance().init();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
