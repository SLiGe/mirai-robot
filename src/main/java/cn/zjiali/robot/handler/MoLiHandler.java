package cn.zjiali.robot.handler;

import cn.zjiali.robot.annotation.Autowired;
import cn.zjiali.robot.config.plugin.MoLiConfig;
import cn.zjiali.robot.service.MoLiService;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;

import java.util.HashMap;
import java.util.Map;

/**
 * 茉莉聊天处理器
 *
 * @author zJiaLi
 * @since 2021-04-08 11:49
 */
public class MoLiHandler extends AbstractHandler {

    @Autowired
    private MoLiService moLiService;

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String msg = getMsg(event);
        Map<String, String> interceptMessage = interceptMessage(msg);
        if ("1".equals(interceptMessage.get("flag"))) {
            event.getGroup().sendMessage(new At(event.getSender().getId()).plus(interceptMessage.get("message")));
            return;
        }
        if ("1".equals(MoLiConfig.chatGroupAt)) {
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
        Map<String, String> interceptMessage = interceptMessage(msg);
        if ("1".equals(interceptMessage.get("flag"))) {
            event.getSender().sendMessage(interceptMessage.get("message"));
            return;
        }
        String sendMsg = moLiService.getCommonChatMessage(msg);
        event.getSender().sendMessage(sendMsg);
    }

    private Map<String, String> interceptMessage(String msg) {
        Map<String, String> retMap = new HashMap<>();
        if ("1".equals(MoLiConfig.jokeEnable) && msg.contains(MoLiConfig.jokeCommand)) {
            String jokeMessage = moLiService.getJokeMessage();
            retMap.put("flag", "1");
            retMap.put("message", jokeMessage);
            return retMap;
        }
        if ("1".equals(MoLiConfig.gylqEnable) && msg.contains(MoLiConfig.gylqCommand)) {
            String gylqMessage = moLiService.getGylqMessage();
            retMap.put("flag", "1");
            retMap.put("message", gylqMessage);
            return retMap;
        }
        if ("1".equals(MoLiConfig.csylqEnable) && msg.contains(MoLiConfig.csylqCommand)) {
            String csylqMessage = moLiService.getCsylqMessage();
            retMap.put("flag", "1");
            retMap.put("message", csylqMessage);
            return retMap;
        }
        if ("1".equals(MoLiConfig.yllqEnable) && msg.contains(MoLiConfig.yllqCommand)) {
            String yllqMessage = moLiService.getYllqMessage();
            retMap.put("flag", "1");
            retMap.put("message", yllqMessage);
            return retMap;
        }
        retMap.put("flag", "0");
        return retMap;
    }
}
