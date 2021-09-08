package cn.zjiali.robot.handler;

import cn.zjiali.robot.annotation.Autowired;
import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.constant.PluginProperty;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.service.MoLiService;
import cn.zjiali.robot.util.PluginConfigUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
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

    @Autowired
    private MoLiService moLiService;

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String msg = getMsg(event);
        Map<String, String> interceptMessage = interceptMessage(event.getSender().getId(), true, event.getGroup().getId(), msg);
        if ("1".equals(interceptMessage.get("flag"))) {
            event.getGroup().sendMessage(new At(event.getSender().getId()).plus(interceptMessage.get("message")));
            return;
        }
        if ("1".equals(PluginConfigUtil.getConfigVal(PluginCode.MOLI, PluginProperty.CHAT_GROUP_AT))) {
            String atNick = "@" + event.getBot().getNick();
            if (msg.contains(atNick)) {
                msg = msg.replace(atNick, "");
                String sendMsg = moLiService.getCommonChatMessage(msg);
                event.getGroup().sendMessage(new At(event.getSender().getId()).plus(sendMsg));
            }
        } else {
            String sendMsg = moLiService.getCommonChatMessage(msg);
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
        String sendMsg = moLiService.getCommonChatMessage(msg);
        event.getSender().sendMessage(sendMsg);
    }

    @Override
    public OutMessage handleGroupMessageEvent(GroupMessageEvent event) {
        String chatGroupAt = PluginConfigUtil.getConfigVal(PluginCode.MOLI, PluginProperty.CHAT_GROUP_AT);
        String msg = getMsg(event);
        if ("1".equals(chatGroupAt)) {
            String atNick = "@" + event.getBot().getNick();
            if (msg.contains(atNick)) {
                msg = msg.replace(atNick, "");
                String sendMsg = moLiService.getCommonChatMessage(msg);
                return OutMessage.builder().pluginCode(PluginCode.MOLI).content(sendMsg).convertFlag(false).build();
            }
        } else {
            String sendMsg = moLiService.getCommonChatMessage(msg);
            return OutMessage.builder().pluginCode(PluginCode.MOLI).content(sendMsg).convertFlag(false).build();
        }
        return super.handleGroupMessageEvent(event);
    }

    @Override
    public OutMessage handleFriendMessageEvent(FriendMessageEvent event) {
        String sendMsg = moLiService.getCommonChatMessage(getMsg(event));
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
}
