package cn.zjiali.robot;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * @author zJiaLi
 * @since 2020-10-29 11:09
 */
public class RobotApplication {

    public static void main(String[] args) {
        Bot bot = BotFactoryJvm.newBot(111L, "111");

        bot.login();

        Events.registerEvents(bot, new SimpleListenerHost() {

            @EventHandler
            public ListeningStatus onGroupMessage(GroupMessageEvent event) {
                String senderName = event.getSenderName();
                System.out.println(senderName);
                return ListeningStatus.LISTENING;
            }

            @EventHandler
            public ListeningStatus onFriendMessage(FriendMessageEvent event) {
                String senderName = event.getSenderName();
                System.out.println(senderName);
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
