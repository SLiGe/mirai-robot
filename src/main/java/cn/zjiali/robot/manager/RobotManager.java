package cn.zjiali.robot.manager;

import cn.zjiali.robot.annotation.Component;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author zJiaLi
 * @since 2021-07-30 16:02
 */
@Component
public class RobotManager {

    private Bot bot;

    public void init(Bot bot) {
        this.bot = bot;
    }

    public void sendFriendMessage(long qq, String message) {
        Friend friend = bot.getFriend(qq);
        if (friend != null) {
            friend.sendMessage(message);
        }
    }

    public void sendGroupMessage(long groupId, String message) {
        sendGroup(groupId, new PlainText(message));
    }

    public void sendGroupAtMessage(long qq, long groupId, String message) {
        sendGroup(groupId, new At(qq).plus(message));
    }

    public void sendGroup(long groupId, Message message) {
        Group group = bot.getGroup(groupId);
        if (group != null) {
            group.sendMessage(message);
        }
    }


    public Bot getBot() {
        return bot;
    }
}
