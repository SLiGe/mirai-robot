package cn.zjiali.robot.main;

import cn.hutool.cron.CronUtil;
import cn.zjiali.robot.RobotApplication;
import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.config.Plugin;
import cn.zjiali.robot.config.PluginTemplate;
import cn.zjiali.robot.constant.Constants;
import cn.zjiali.robot.guice.module.GlobalEventHandlerModule;
import cn.zjiali.robot.guice.module.HandlerInterceptorModule;
import cn.zjiali.robot.guice.module.ManagerModule;
import cn.zjiali.robot.guice.module.SimpleMessageEventHandlerModule;
import cn.zjiali.robot.guice.provider.RpcManagedChannelProvider;
import cn.zjiali.robot.guice.provider.RpcServiceProvider;
import cn.zjiali.robot.main.websocket.WebSocketManager;
import cn.zjiali.robot.manager.PluginManager;
import cn.zjiali.robot.manager.ServerConfigManager;
import cn.zjiali.robot.manager.ServerTokenManager;
import cn.zjiali.robot.model.ApplicationConfig;
import cn.zjiali.robot.model.SystemConfig;
import cn.zjiali.robot.task.RobotOnlineStatusTask;
import cn.zjiali.robot.util.CommonLogger;
import cn.zjiali.robot.util.GuiceUtil;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.PropertiesUtil;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.grpc.ManagedChannel;

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

    private static final ApplicationBootStrap applicationBootStrap = new ApplicationBootStrap();
    private final CommonLogger commonLogger = new CommonLogger(ApplicationBootStrap.class);
    private Injector injector;

    private ApplicationBootStrap() {
    }

    public static ApplicationBootStrap getInstance() {
        return applicationBootStrap;
    }

    public Injector getInjector() {
        return injector;
    }

    public void init() throws IOException {
        loadAppConfig();
        initGuiceContext();
        genServerToken();
        refreshPlugin();
        loadMessageTemplate();
        loadServerConfig();
        new Thread(() -> {
            try {
                RobotApplication.initLatch.await();
                loadWebSocket();
                loadCronTask();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void refreshPlugin() {
        PluginManager pluginManager = this.injector.getInstance(PluginManager.class);
        pluginManager.refreshPlugin();
    }

    private void genServerToken() {
        GuiceUtil.getBean(ServerTokenManager.class).genServerToken();
    }

    private void initGuiceContext() {
        this.injector = Guice.createInjector(new RpcManagedChannelProvider(), new RpcServiceProvider(),
                new ManagerModule(), new SimpleMessageEventHandlerModule(),
                new GlobalEventHandlerModule(),
                new HandlerInterceptorModule());
    }

    /**
     * 加载消息模板
     */
    private void loadMessageTemplate() {
        InputStream configStream = ApplicationBootStrap.class.getResourceAsStream("/system-config.json");
        if (configStream == null)
            throw new RuntimeException("加载消息模板出错,请检查配置文件system-config.json是否存在!");
        String systemConfigJson = new BufferedReader(new InputStreamReader(configStream, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        SystemConfig systemConfig = JsonUtil.json2obj(systemConfigJson, SystemConfig.class);
        Map<String, List<String>> messageTemplateMap = systemConfig.getMessageTemplates();
        PluginTemplate pluginTemplate = PluginTemplate.getInstance();
        List<Plugin> plugins = AppConfig.getApplicationConfig().getPlugins();
        Objects.requireNonNull(plugins).stream().filter(plugin -> !Constants.N.equals(plugin.getTemplateFlag()))
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

    /**
     * 加载WebSocket
     */
    private void loadWebSocket() throws IOException {
        String webSocketFlag = PropertiesUtil.getApplicationProperty("robot.websocket.flag");
        if (Constants.Y.equals(webSocketFlag)) {
            commonLogger.info("[WebSocket]====加载中");
            WebSocketManager webSocketManager = this.injector.getInstance(WebSocketManager.class);
            webSocketManager.connect();
            commonLogger.info("[WebSocket]====加载完成");
            hookShutdownWebSocket();

        }

    }

    private void hookShutdownWebSocket() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            var managedChannel = GuiceUtil.getBean(ManagedChannel.class);
            managedChannel.shutdown();
            var webSocketManager = GuiceUtil.getBean(WebSocketManager.class);
            webSocketManager.close();
        }));
    }


    /**
     * 加载应用配置
     *
     * @throws IOException
     */
    private void loadAppConfig() throws IOException {
        InputStream configStream = null;
        String systemConfigFileLocal = System.getProperty("application.config.file.local");
        String serverEnv = System.getProperty("server.env");
        // 是否启用本地配置文件
        String localConfigFileFlag = PropertiesUtil.getProperty("application.properties", "application.config.file.local");
        if (("true".equals(localConfigFileFlag) || "true".equals(systemConfigFileLocal)) && !"dev".equals(serverEnv)) {
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
        AppConfig.applicationConfig = JsonUtil.json2obj(appConfigJson, ApplicationConfig.class);
        configStream.close();
    }

    /**
     * 加载服务端配置
     */
    private void loadServerConfig() {
        if (AppConfig.applicationConfig.serverControl()) {
            ServerConfigManager serverConfigManager = GuiceUtil.getBean(ServerConfigManager.class);
            serverConfigManager.pullServerConfig();
        }
    }

    /**
     * 加载定时任务
     *
     * @throws IOException e
     */
    private void loadCronTask() throws IOException {
        String webSocketFlag = PropertiesUtil.getApplicationProperty("robot.websocket.flag");
        if (AppConfig.serverControl() || Constants.Y.equals(webSocketFlag)) {
            CronUtil.setMatchSecond(true);
            CronUtil.start();
        }
        CronUtil.schedule("0 0/2 * * * ?", new RobotOnlineStatusTask());
        if (AppConfig.serverControl()) {
            CronUtil.schedule("0 0 0/5 * * ?", (Runnable) () -> GuiceUtil.getBean(ServerTokenManager.class).genServerToken());
        }
    }


}
