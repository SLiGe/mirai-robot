package cn.zjiali.robot;

import cn.zjiali.robot.annotation.Application;
import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.handler.MessageHandler;
import cn.zjiali.robot.main.ApplicationBootStrap;
import kotlin.Unit;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.*;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.MiraiLogger;
import net.mamoe.mirai.utils.PlatformLogger;


/**
 * @author zJiaLi
 * @since 2020-10-29 11:09
 */
@Application(basePackages = {"cn.zjiali.robot"})
public class RobotApplication {

    private static final MiraiLogger miraiLogger = new PlatformLogger(RobotApplication.class.getName());

    static {
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
        long qq = Long.parseLong(AppConfig.applicationConfig.getQq());
        String password = AppConfig.applicationConfig.getPassword();
        Bot bot = BotFactory.INSTANCE.newBot(qq, password, new BotConfiguration() {
            {
                fileBasedDeviceInfo("deviceInfo.json");
            }
        });

        bot.login();
        EventChannel<BotEvent> eventChannel = bot.getEventChannel();
        // 创建监听
        eventChannel.exceptionHandler(e -> {
            e.printStackTrace();
            return Unit.INSTANCE;
        });
        eventChannel.subscribeAlways(GroupMessageEvent.class, MessageHandler::handleGroupMessage);
        eventChannel.subscribeAlways(FriendMessageEvent.class, MessageHandler::handleFriendMessage);
        bot.join(); // 阻塞当前线程直到 bot 离线
    }

}
