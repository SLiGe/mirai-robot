package cn.zjiali.robot.main;

import cn.hutool.cron.CronUtil;
import cn.zjiali.robot.RobotApplication;
import cn.zjiali.robot.annotation.Application;
import cn.zjiali.robot.annotation.Component;
import cn.zjiali.robot.annotation.Service;
import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.config.Plugin;
import cn.zjiali.robot.config.PluginTemplate;
import cn.zjiali.robot.factory.DefaultBeanFactory;
import cn.zjiali.robot.factory.MessageEventHandlerFactory;
import cn.zjiali.robot.factory.ServiceFactory;
import cn.zjiali.robot.handler.MessageEventHandler;
import cn.zjiali.robot.main.websocket.WebSocketManager;
import cn.zjiali.robot.model.ApplicationConfig;
import cn.zjiali.robot.model.SystemConfig;
import cn.zjiali.robot.util.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 启动引导
 *
 * @author zJiaLi
 * @since 2021-04-07 21:37
 */
public class ApplicationBootStrap {

    private final CommonLogger commonLogger = new CommonLogger(ApplicationBootStrap.class.getName(), ApplicationBootStrap.class);

    private ApplicationBootStrap() {
    }

    private static final ApplicationBootStrap applicationBootStrap = new ApplicationBootStrap();

    public static ApplicationBootStrap getInstance() {
        return applicationBootStrap;
    }

    public void init() throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        loadService();
        loadAppConfig();
        loadMessageTemplate();
        fillBeanDefinition();
        loadWebSocket();
    }

    /**
     * 加载消息模板
     */
    private void loadMessageTemplate() {
        InputStream configStream = ApplicationBootStrap.class.getResourceAsStream("/system-config.json");
        if (configStream == null) throw new RuntimeException("加载消息模板出错,请检查配置文件system-config.json是否存在!");
        String systemConfigJson = new BufferedReader(new InputStreamReader(configStream, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        SystemConfig systemConfig = JsonUtil.json2obj(systemConfigJson, SystemConfig.class);
        Map<String, List<String>> messageTemplateMap = systemConfig.getMessageTemplates();
        PluginTemplate pluginTemplate = PluginTemplate.getInstance();
        List<Plugin> plugins = AppConfig.getApplicationConfig().getPlugins();
        Objects.requireNonNull(plugins).stream().filter(plugin -> !"0".equals(plugin.getTemplateFlag()))
                .forEach(plugin -> {
                    if ("1".equals(plugin.getTemplateFlag())) {
                        String template = plugin.getTemplate();
                        pluginTemplate.putTemplate(plugin.getCode(), template);
                    } else if ("2".equals(plugin.getTemplateFlag())) {
                        List<String> templates = null;
                        if (messageTemplateMap != null) {
                            templates = messageTemplateMap.get(plugin.getCode());
                            templates.forEach(messageTemplateCode -> {
                                String templateText = Objects.requireNonNull(plugin.getProperties()).get(messageTemplateCode);
                                pluginTemplate.putTemplate(messageTemplateCode, templateText);
                            });
                        }
                    }
                });
    }

    private void fillBeanDefinition() {
        DefaultBeanFactory.getInstance().fillBeanDefinitionFields();
    }

    /**
     * 加载WebSocket
     */
    private void loadWebSocket() throws IOException {
        String webSocketFlag = PropertiesUtil.getApplicationProperty("robot.websocket.flag");
        if ("1".equals(webSocketFlag)) {
            commonLogger.info("[WebSocket]====加载中");
            WebSocketManager webSocketManager = ServiceFactory.getInstance().getBean(WebSocketManager.class.getSimpleName(), WebSocketManager.class);
            webSocketManager.connect();
            //添加定时任务定时确认websocket连接状态
            CronUtil.setMatchSecond(true);
            CronUtil.start();
            commonLogger.info("[WebSocket]====加载完成");
        }

    }

    /**
     * 加载业务类
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
                    Component component = aClass.getAnnotation(Component.class);
                    if (service != null) {
                        Object instance = aClass.newInstance();
                        String serviceName = "".equals(service.name()) ? instance.getClass().getSimpleName() : service.name();
                        ServiceFactory.getInstance().put(serviceName, instance);
                    } else if (component != null) {
                        Object instance = aClass.newInstance();
                        String serviceName = "".equals(component.name()) ? instance.getClass().getSimpleName() : component.name();
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
                configStream = Files.newInputStream(configFile.toPath());
            }
        } else {
            String appProfile = PropertiesUtil.getProperty("application.properties", "application.profile");
            configStream = ApplicationBootStrap.class.getResourceAsStream("/application-" + appProfile + ".json");
        }
        if (configStream == null) {
            commonLogger.error("[loadAppConfig]====加载配置文件失败,自动退出!");
            System.exit(0);
        }
        String appConfigJson = new BufferedReader(new InputStreamReader(configStream, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        final ApplicationConfig applicationConfig = JsonUtil.json2obj(appConfigJson, ApplicationConfig.class);
        AppConfig.applicationConfig = applicationConfig;
        final List<Plugin> plugins = applicationConfig.getPlugins();
        if (plugins != null) {
            for (Plugin plugin : plugins) {
                String pluginName = plugin.getName();
                String pluginHandler = plugin.getHandler();
                int pluginEnable = plugin.getEnable();
                if (pluginEnable == 1) {
                    MessageEventHandler messageEventHandler = MessageEventHandlerFactory.getInstance().put(pluginName, pluginHandler);
                    AppConfig.msgMessageEventHandlers.add(messageEventHandler);
                    commonLogger.info("[loadAppConfig]====加载 [{}] 成功！", pluginName);
                }
            }
        }
        configStream.close();
    }


}
