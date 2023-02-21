package cn.zjiali.robot;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.handler.GlobalMessageHandler;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.main.system.SysLoginSolver;
import cn.zjiali.robot.main.websocket.WebSocketManager;
import cn.zjiali.robot.manager.RobotManager;
import cn.zjiali.robot.util.CommonLogger;
import cn.zjiali.robot.util.GuiceUtil;
import cn.zjiali.robot.util.ObjectUtil;
import io.grpc.ManagedChannel;
import kotlin.Unit;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.LoggerAdapters;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;


/**
 * 应用启动类
 *
 * @author zJiaLi
 * @since 2020-10-29 11:09
 */
public class RobotApplication {

    public static final CountDownLatch initLatch = new CountDownLatch(1);
    private static final CommonLogger logger = new CommonLogger(RobotApplication.class);

    private static void init() {
        logger.info("====初始化配置中====");
        long startInitTime = System.currentTimeMillis();
        try {
            ApplicationBootStrap.getInstance().init();
            logger.info("====初始化配置完成==== 共耗时: {} ms ", (System.currentTimeMillis() - startInitTime));
            logger.info("⭐⭐⭐⭐⭐⭐GitHub: https://github.com/SLiGe/mirai-robot ⭐⭐⭐⭐⭐⭐");
        } catch (Exception e) {
            logger.error("====初始化配置出错,e: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LoggerAdapters.useLog4j2();
        init();
        long qq = Long.parseLong(Objects.requireNonNull(AppConfig.applicationConfig.getQq()));
        String password = AppConfig.applicationConfig.getPassword();
        assert password != null;
        Bot bot = BotFactory.INSTANCE.newBot(qq, password, new BotConfiguration() {
            {
                //设置登录解决器
                setLoginSolver(GuiceUtil.getBean(SysLoginSolver.class));
                // 选择协议
                setProtocol(switchProtocol());
                setCacheDir(new File(System.getProperty("robot.workdir") + "/cache"));
                setWorkingDir(new File(System.getProperty("robot.workdir")));
                fileBasedDeviceInfo("device.json");
            }
        });
        bot.login();
        initLatch.countDown();
        EventChannel<BotEvent> eventChannel = bot.getEventChannel();
        // 创建监听
        eventChannel.exceptionHandler(e -> {
            logger.error("unknown error: {}", ExceptionUtil.stacktraceToString(e));
            return Unit.INSTANCE;
        });
        //保存robot实例
        final RobotManager robotManager = GuiceUtil.getBean(RobotManager.class);
        robotManager.init(bot);
        final GlobalMessageHandler globalMessageHandler = GuiceUtil.getBean(GlobalMessageHandler.class);
        eventChannel.subscribeAlways(GroupMessageEvent.class, globalMessageHandler::handleGroupMessageEvent);
        eventChannel.subscribeAlways(FriendMessageEvent.class, globalMessageHandler::handleFriendMessageEvent);
        eventChannel.subscribeAlways(StrangerMessageEvent.class, globalMessageHandler::handleOtherMessageEvent);
        eventChannel.subscribeAlways(NewFriendRequestEvent.class, NewFriendRequestEvent::accept);
        eventChannel.subscribeAlways(GroupTempMessageEvent.class, globalMessageHandler::handleOtherMessageEvent);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            var managedChannel = GuiceUtil.getBean(ManagedChannel.class);
            if (managedChannel != null) {
                managedChannel.shutdown();
            }
            var webSocketManager = GuiceUtil.getBean(WebSocketManager.class);
            webSocketManager.close();
        }));
        bot.join(); // 阻塞当前线程直到 bot 离线
    }

    /**
     * 选择协议
     *
     * @return 协议
     */
    private static BotConfiguration.MiraiProtocol switchProtocol() {
        String robotProtocol = System.getProperty("robot.protocol");
        if (!ObjectUtil.isNullOrEmpty(robotProtocol)) {
            switch (robotProtocol) {
                case "0" -> {
                    return (BotConfiguration.MiraiProtocol.ANDROID_PHONE);
                }
                case "1" -> {
                    return (BotConfiguration.MiraiProtocol.ANDROID_PAD);
                }
                case "2" -> {
                    return (BotConfiguration.MiraiProtocol.ANDROID_WATCH);
                }
                case "3" -> {
                    return (BotConfiguration.MiraiProtocol.IPAD);
                }
                case "4" -> {
                    return (BotConfiguration.MiraiProtocol.MACOS);
                }
            }
        }
        return (BotConfiguration.MiraiProtocol.ANDROID_PHONE);
    }

}
