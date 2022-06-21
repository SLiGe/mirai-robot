package cn.zjiali.robot.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.zjiali.robot.constant.ApiUrl;
import cn.zjiali.robot.constant.AppConstants;
import cn.zjiali.robot.main.OutMessageConvert;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.model.response.RobotBaseResponse;
import cn.zjiali.robot.model.response.server.PluginInfo;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.ObjectUtil;
import cn.zjiali.robot.util.PropertiesUtil;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务端消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 21:20
 */
public class ServerGlobalMessageHandler implements GlobalMessageHandler {

    @Inject
    private Set<MessageEventHandler> messageEventHandlers;

    public void handleGroupMessageEvent(GroupMessageEvent event) {
        handleMessage(true, event, null);
    }

    public void handleFriendMessageEvent(FriendMessageEvent event) {
        handleMessage(false, null, event);
    }

    @Override
    public void handleOtherMessageEvent(MessageEvent event) {
        handleMessage(false, null, event);
    }

    private void handleMessage(boolean isGroup, GroupMessageEvent groupMessageEvent, MessageEvent friendMessageEvent) {
        boolean preHandle = doPreHandle(isGroup ? groupMessageEvent : friendMessageEvent);
        if (!preHandle) return;
        String msg = isGroup ? groupMessageEvent.getMessage().contentToString() : friendMessageEvent.getMessage().contentToString();
        ArrayList<MessageEventHandler> messageEventHandlerList = filterMessageEventHandlerList(isGroup, groupMessageEvent);
        List<OutMessage> outMessageList = Lists.newLinkedList();
        for (MessageEventHandler messageEventHandler : messageEventHandlerList) {
            if (messageEventHandler.matchCommand(msg) && !messageEventHandler.ignore(msg)) {
                OutMessage outMessage;
                if (isGroup) {
                    outMessage = messageEventHandler.handleGroupMessageEvent(groupMessageEvent);
                    if (outMessage == null) continue;
                    int messageType = outMessage.getMessageType();
                    if (messageType == AppConstants.MESSAGE_TYPE_HANDLER) {
                        String message = OutMessageConvert.getInstance().convert(outMessage);
                        if (!ObjectUtil.isNullOrEmpty(message))
                            groupMessageEvent.getGroup().sendMessage(new At(groupMessageEvent.getSender().getId()).plus(message));
                    } else if (messageType == AppConstants.MESSAGE_TYPE_PLUGIN) {
                        if (outMessage.getMessage() != null) {
                            groupMessageEvent.getGroup().sendMessage(outMessage.getMessage());
                        }
                    }
                } else {
                    outMessage = messageEventHandler.handleFriendMessageEvent((FriendMessageEvent) friendMessageEvent);
                    if (outMessage == null) continue;
                    int messageType = outMessage.getMessageType();
                    if (messageType == AppConstants.MESSAGE_TYPE_HANDLER) {
                        String message = OutMessageConvert.getInstance().convert(outMessage);
                        if (!ObjectUtil.isNullOrEmpty(message)) friendMessageEvent.getSender().sendMessage(message);
                    } else if (messageType == AppConstants.MESSAGE_TYPE_PLUGIN) {
                        if (outMessage.getMessage() != null) {
                            friendMessageEvent.getSender().sendMessage(outMessage.getMessage());
                        }
                    }
                }
                if (ObjectUtil.isNotNullOrEmpty(outMessage)) {
                    outMessageList.add(outMessage);
                }
                if (!messageEventHandler.next()) break;
            }
        }
        triggerAfterCompletion(isGroup ? groupMessageEvent : friendMessageEvent, outMessageList);
    }

    /**
     * 过滤消息处理器
     *
     * @param isGroup 是否群
     * @return 消息处理器
     */
    private ArrayList<MessageEventHandler> filterMessageEventHandlerList(boolean isGroup, GroupMessageEvent groupMessageEvent) {
        ArrayList<MessageEventHandler> messageEventHandlerList = Lists.newArrayList(messageEventHandlers);
        if (isGroup) {
            long groupNumber = groupMessageEvent.getGroup().getId();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("groupNumber", groupNumber);
            String serverGroupPluginJson = HttpUtil.post(PropertiesUtil.getApiProperty(ApiUrl.QUERY_GROUP_PLUGIN), JsonUtil.obj2str(jsonObject));
            RobotBaseResponse<List<PluginInfo>> pluginInfoResponse = JsonUtil.toObjByType(serverGroupPluginJson, new TypeToken<RobotBaseResponse<List<PluginInfo>>>() {
            }.getType());
            List<PluginInfo> serverGroupPluginList = pluginInfoResponse.getData();
            if (CollectionUtil.isNotEmpty(serverGroupPluginList)) {
                Set<String> pluginCodeSet = serverGroupPluginList.stream().map(PluginInfo::getPluginCode).collect(Collectors.toSet());
                messageEventHandlerList.removeIf(messageEventHandler -> {
                    String code = messageEventHandler.code();
                    return !pluginCodeSet.contains(code);
                });
            }
        }
        //处理器排序
        messageEventHandlerList.sort(Comparator.comparingInt(MessageEventHandler::order));
        return messageEventHandlerList;
    }


}
