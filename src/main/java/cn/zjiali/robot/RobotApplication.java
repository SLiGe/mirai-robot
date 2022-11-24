package cn.zjiali.robot;

import cn.zjiali.robot.annotation.Application;
import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.handler.GlobalMessageHandler;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.main.system.SysLoginSolver;
import cn.zjiali.robot.manager.RobotManager;
import cn.zjiali.robot.util.GuiceUtil;
import cn.zjiali.robot.util.ObjectUtil;
import kotlin.Unit;
import kotlinx.serialization.json.Json;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.*;
import xyz.cssxsh.mirai.device.MiraiDeviceGenerator;

import java.io.File;
import java.util.Objects;


/**
 * 应用启动类
 *
 * @author zJiaLi
 * @since 2020-10-29 11:09
 */
@Application(basePackages = {"cn.zjiali.robot.service", "cn.zjiali.robot.main", "cn.zjiali.robot.manager"})
public class RobotApplication {

    private static final MiraiLogger miraiLogger = new PlatformLogger(RobotApplication.class.getName());

    private static void init() {
        miraiLogger.info("====初始化配置中====");
        try {
            ApplicationBootStrap.getInstance().init();
            miraiLogger.info("====初始化配置完成====");
            miraiLogger.info("⭐⭐⭐⭐⭐⭐GitHub: https://github.com/SLiGe/mirai-robot ⭐⭐⭐⭐⭐⭐");
        } catch (Exception e) {
            miraiLogger.error("====初始化配置出错,e: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        init();
        long qq = Long.parseLong(Objects.requireNonNull(AppConfig.applicationConfig.getQq()));
        String password = AppConfig.applicationConfig.getPassword();
        assert password != null;
        Bot bot = BotFactory.INSTANCE.newBot(qq, password, new BotConfiguration() {
            {
                //加载设备信息
//                loadDeviceInfoJson(Objects.requireNonNull(DeviceUtil.getDeviceInfoJson(AppConfig.applicationConfig.getQq())));
                //设置登录解决器
                setLoginSolver(GuiceUtil.getBean(SysLoginSolver.class));
                // 选择协议
                setProtocol(switchProtocol());
                setCacheDir(new File("/cache"));
                setWorkingDir(new File(System.getProperty("robot.workdir")));
//                fileBasedDeviceInfo(System.getProperty("robot.workdir") + "/deviceInfo.json");
            }
        });
        DeviceInfo deviceInfo = new MiraiDeviceGenerator().load(bot);
        bot.getConfiguration().loadDeviceInfoJson(DeviceInfoManager.INSTANCE.serialize(deviceInfo, Json.Default));
        bot.login();
        EventChannel<BotEvent> eventChannel = bot.getEventChannel();
        // 创建监听
        eventChannel.exceptionHandler(e -> {
            e.printStackTrace();
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
                case "0":
                    return (BotConfiguration.MiraiProtocol.ANDROID_PHONE);
                case "1":
                    return (BotConfiguration.MiraiProtocol.ANDROID_PAD);
                case "2":
                    return (BotConfiguration.MiraiProtocol.ANDROID_WATCH);
                case "3":
                    return (BotConfiguration.MiraiProtocol.IPAD);
                case "4":
                    return (BotConfiguration.MiraiProtocol.MACOS);

            }
        }
        return (BotConfiguration.MiraiProtocol.ANDROID_PHONE);
    }

}
