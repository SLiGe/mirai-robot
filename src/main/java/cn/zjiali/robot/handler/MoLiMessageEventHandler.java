package cn.zjiali.robot.handler;

import cn.hutool.core.util.StrUtil;
import cn.zjiali.robot.constant.AppConstants;
import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.constant.PluginProperty;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.service.MoLiService;
import cn.zjiali.robot.util.PluginConfigUtil;
import com.google.inject.Inject;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;

import java.util.ArrayList;

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
    public OutMessage handleGroupMessageEvent(GroupMessageEvent event) {
        String msg = getMsg(event);
        long senderId = event.getSender().getId();
        long groupId = event.getGroup().getId();
        String chatGroupAt = getConfigVal(PluginCode.MOLI, PluginProperty.CHAT_GROUP_AT, event.getGroup().getId(), senderId);
        if ("1".equals(chatGroupAt)) {
            MessageChain message = event.getMessage();
            StringBuilder messageBuilder = new StringBuilder();
            for (SingleMessage singleMessage : message) {
                if (singleMessage instanceof At) {
                    At at = (At) singleMessage;
                    String content = at.contentToString();
                    String sendMsg = moLiService.getCommonChatMessage(groupId, senderId, content);
                    messageBuilder.append(sendMsg);
                }
            }
            String toMessageContent = messageBuilder.toString();
            if (StrUtil.isBlank(toMessageContent)) {
                return null;
            }
            Message toMessage = new PlainText(messageBuilder.toString());
            return OutMessage.builder().pluginCode(PluginCode.MOLI).message(toMessage).messageType(AppConstants.MESSAGE_TYPE_PLUGIN).convertFlag(false).build();
        } else {
            String sendMsg = moLiService.getCommonChatMessage(groupId, senderId, msg);
            return OutMessage.builder().pluginCode(PluginCode.MOLI).content(sendMsg).convertFlag(false).build();
        }
    }

    @Override
    public OutMessage handleFriendMessageEvent(FriendMessageEvent event) {
        String sendMsg = moLiService.getCommonChatMessage(-1, event.getSender().getId(), getMsg(event));
        return OutMessage.builder().pluginCode(PluginCode.MOLI).content(sendMsg).convertFlag(false).build();
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
