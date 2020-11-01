package cn.zjiali.robot;

import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.config.ConfigLoader;
import cn.zjiali.robot.handler.MessageHandler;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendAddEvent;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author zJiaLi
 * @since 2020-10-29 11:09
 */
public class RobotApplication {

    static {
        try {
            ConfigLoader.loadAppConfig();
        } catch (Exception e) {
            System.err.println("[loadAppConfig]====加载应用配置出错! e: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        long qq = Long.parseLong(AppConfig.applicationConfig.getQq());
        String password = AppConfig.applicationConfig.getPassword();
        Bot bot = BotFactoryJvm.newBot(qq, password, new BotConfiguration() {
            {
                fileBasedDeviceInfo("deviceInfo.json");
            }
        });

        bot.login();

        Events.registerEvents(bot, new SimpleListenerHost() {

            @EventHandler
            public ListeningStatus onGroupMessage(GroupMessageEvent event) {
                MessageHandler.handleGroupMessage(event);
                return ListeningStatus.LISTENING;
            }


            @EventHandler
            public ListeningStatus onFriendMessage(FriendMessageEvent event) {
                MessageHandler.handleFriendMessage(event);
                return ListeningStatus.LISTENING;
            }

            @Override
            public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                super.handleException(context, exception);
            }
        });
        bot.join(); // 阻塞当前线程直到 bot 离线
    }

}
