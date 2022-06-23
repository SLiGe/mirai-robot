package cn.zjiali.robot.handler;

import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.constant.PluginProperty;
import cn.zjiali.robot.manager.PluginManager;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.service.MoLiService;
import cn.zjiali.robot.util.PluginConfigUtil;
import com.google.inject.Inject;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 茉莉聊天处理器
 *
 * @author zJiaLi
 * @since 2021-04-08 11:49
 */
public class MoLiMessageEventHandler extends AbstractMessageEventHandler {

    @Inject
    private MoLiService moLiService;

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String msg = getMsg(event);
        Map<String, String> interceptMessage = interceptMessage(event.getSender().getId(), true, event.getGroup().getId(), msg);
        if ("1".equals(interceptMessage.get("flag"))) {
            event.getGroup().sendMessage(new At(event.getSender().getId()).plus(interceptMessage.get("message")));
            return;
        }
        long senderId = event.getSender().getId();
        if ("1".equals(PluginConfigUtil.getConfigVal(PluginCode.MOLI, PluginProperty.CHAT_GROUP_AT))) {
            String atNick = "@" + event.getBot().getNick();
            if (msg.contains(atNick)) {
                msg = msg.replace(atNick, "");
                String sendMsg = moLiService.getCommonChatMessage(senderId, msg);
                event.getGroup().sendMessage(new At(event.getSender().getId()).plus(sendMsg));
            }
        } else {
            String sendMsg = moLiService.getCommonChatMessage(senderId, msg);
            event.getGroup().sendMessage(new At(event.getSender().getId()).plus(sendMsg));
        }
    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        String msg = getMsg(event);
        Map<String, String> interceptMessage = interceptMessage(event.getSender().getId(), false, 0L, msg);
        if ("1".equals(interceptMessage.get("flag"))) {
            event.getSender().sendMessage(interceptMessage.get("message"));
            return;
        }
        String sendMsg = moLiService.getCommonChatMessage(event.getSender().getId(), msg);
        event.getSender().sendMessage(sendMsg);
    }

    @Override
    public OutMessage handleGroupMessageEvent(GroupMessageEvent event) {
        String chatGroupAt = PluginConfigUtil.getConfigVal(PluginCode.MOLI, PluginProperty.CHAT_GROUP_AT);
        String msg = getMsg(event);
        long senderId = event.getSender().getId();
        if ("1".equals(chatGroupAt)) {
            String atNick = "@" + event.getBot().getNick();
            if (event.getMessage().contains(new At(event.getBot().getId()))) {
                msg = msg.replace(atNick, "");
                String sendMsg = moLiService.getCommonChatMessage(senderId, msg);
                return OutMessage.builder().pluginCode(PluginCode.MOLI).content(sendMsg).convertFlag(false).build();
            }
        } else {
            String sendMsg = moLiService.getCommonChatMessage(event.getSender().getId(), msg);
            return OutMessage.builder().pluginCode(PluginCode.MOLI).content(sendMsg).convertFlag(false).build();
        }
        return super.handleGroupMessageEvent(event);
    }

    @Override
    public OutMessage handleFriendMessageEvent(FriendMessageEvent event) {
        String sendMsg = moLiService.getCommonChatMessage(event.getSender().getId(), getMsg(event));
        return OutMessage.builder().pluginCode(PluginCode.MOLI).content(sendMsg).convertFlag(false).build();
    }

    private Map<String, String> interceptMessage(long qq, boolean isGroup, long groupNum, String msg) {
        Map<String, String> retMap = new HashMap<>();
        if (PluginConfigUtil.getEnable(PluginCode.JOKE) == 1
                && msg.contains(PluginConfigUtil.getCommand(PluginCode.JOKE))) {
            String jokeMessage = moLiService.getJokeMessage(qq, isGroup, groupNum);
            retMap.put("flag", "1");
            retMap.put("message", jokeMessage);
            return retMap;
        }
        if (PluginConfigUtil.getEnable(PluginCode.GY_LQ) == 1
                && msg.contains(PluginConfigUtil.getCommand(PluginCode.GY_LQ))) {
            String gylqMessage = moLiService.getGylqMessage(qq, isGroup, groupNum);
            retMap.put("flag", "1");
            retMap.put("message", gylqMessage);
            return retMap;
        }
        if (PluginConfigUtil.getEnable(PluginCode.CSY_LQ) == 1
                && msg.contains(PluginConfigUtil.getCommand(PluginCode.CSY_LQ))) {
            String csylqMessage = moLiService.getCsylqMessage(qq, isGroup, groupNum);
            retMap.put("flag", "1");
            retMap.put("message", csylqMessage);
            return retMap;
        }
        if (PluginConfigUtil.getEnable(PluginCode.YL_LQ) == 1
                && msg.contains(PluginConfigUtil.getCommand(PluginCode.YL_LQ))) {
            String yllqMessage = moLiService.getYllqMessage(qq, isGroup, groupNum);
            retMap.put("flag", "1");
            retMap.put("message", yllqMessage);
            return retMap;
        }
        retMap.put("flag", "0");
        return retMap;
    }

    @Override
    public boolean next() {
        return true;
    }

    @Override
    public boolean matchCommand(String command) {
        return true;
    }

    @Override
    public boolean ignore(String msg) {
        ArrayList<String> ignoreWords = PluginConfigUtil.getIgnoreWords(PluginCode.MOLI);
        return ignoreWords.contains(msg);
    }

    @Override
    public boolean ignore(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage().contentToString();
        if (messageEvent instanceof GroupMessageEvent) {
            long groupId = ((GroupMessageEvent) messageEvent).getGroup().getId();
            long senderId = messageEvent.getSender().getId();
            ArrayList<String> ignoreWords = pluginManager.getIgnoreWords(PluginCode.MOLI, groupId, senderId);
            return ignoreWords.contains(msg);
        }
        return ignore(msg);
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
