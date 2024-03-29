package cn.zjiali.robot.model.message;

import cn.zjiali.robot.constant.AppConstants;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Message;

import java.util.Map;

/**
 * @author zJiaLi
 * @since 2021-09-04 11:03
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public class OutMessage {

    /**
     * 消息内容
     */
    private String content;

    /**
     * 插件编码
     */
    private String pluginCode;

    /**
     * 转换标志
     */
    private boolean convertFlag;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 填充消息标志 --> 1. fillMap 2. fillObj
     */
    private int fillFlag;

    /**
     * 填充Map
     */
    private Map<String, String> fillMap;

    /**
     * 填充对象
     */
    private Object fillObj;

    private String finalMessage;

    private Message message;

    /**
     * 1 总处理器转换消息 2 插件自提供消息
     */
    private int messageType;

    /**
     * 群号
     */
    private long groupId;

    /**
     * 发消息人
     */
    private long senderId;

    /**
     * 来源消息类型 1 好友 2 群组
     */
    private int fromMsgType;

    private boolean empty;

    OutMessage(String content, String pluginCode, boolean convertFlag, String templateCode, int fillFlag, Map<String, String> fillMap, Object fillObj, String finalMessage, int messageType, Message message, long groupId, long senderId, int fromMsgType) {
        this.content = content;
        this.pluginCode = pluginCode;
        this.convertFlag = convertFlag;
        this.templateCode = templateCode;
        this.fillFlag = fillFlag;
        this.fillMap = fillMap;
        this.fillObj = fillObj;
        this.finalMessage = finalMessage;
        this.messageType = messageType;
        this.message = message;
        this.groupId = groupId;
        this.senderId = senderId;
        this.fromMsgType = fromMsgType;
    }

    public OutMessage() {

    }

    public static OutMessageBuilder builder() {
        return new OutMessageBuilder();
    }

    public static OutMessage empty() {
        OutMessage outMessage = new OutMessage();
        outMessage.setEmpty(Boolean.TRUE);
        return outMessage;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public int getFromMsgType() {
        return fromMsgType;
    }

    public void setFromMsgType(int fromMsgType) {
        this.fromMsgType = fromMsgType;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPluginCode() {
        return this.pluginCode;
    }

    public void setPluginCode(String pluginCode) {
        this.pluginCode = pluginCode;
    }

    public boolean isConvertFlag() {
        return this.convertFlag;
    }

    public void setConvertFlag(boolean convertFlag) {
        this.convertFlag = convertFlag;
    }

    public String getTemplateCode() {
        return this.templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public int getFillFlag() {
        return this.fillFlag;
    }

    public void setFillFlag(int fillFlag) {
        this.fillFlag = fillFlag;
    }

    public Map<String, String> getFillMap() {
        return this.fillMap;
    }

    public void setFillMap(Map<String, String> fillMap) {
        this.fillMap = fillMap;
    }

    public Object getFillObj() {
        return this.fillObj;
    }

    public void setFillObj(Object fillObj) {
        this.fillObj = fillObj;
    }

    public String getFinalMessage() {
        return this.finalMessage;
    }

    public void setFinalMessage(String finalMessage) {
        this.finalMessage = finalMessage;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


    public static class OutMessageBuilder {
        private String content;
        private String pluginCode;
        private boolean convertFlag;
        private String templateCode;
        private int fillFlag;
        private Map<String, String> fillMap;
        private Object fillObj;
        private String finalMessage;

        private Message message;

        private int messageType = 1;

        private long groupId;

        private long senderId;

        private int fromMsgType;

        OutMessageBuilder() {
        }

        public OutMessageBuilder content(String content) {
            this.content = content;
            return this;
        }

        public OutMessageBuilder pluginCode(String pluginCode) {
            this.pluginCode = pluginCode;
            return this;
        }

        public OutMessageBuilder convertFlag(boolean convertFlag) {
            this.convertFlag = convertFlag;
            return this;
        }

        public OutMessageBuilder templateCode(String templateCode) {
            this.templateCode = templateCode;
            return this;
        }

        public OutMessageBuilder fillFlag(int fillFlag) {
            this.fillFlag = fillFlag;
            return this;
        }

        public OutMessageBuilder fillMap(Map<String, String> fillMap) {
            this.fillMap = fillMap;
            return this;
        }

        public OutMessageBuilder fillObj(Object fillObj) {
            this.fillObj = fillObj;
            return this;
        }

        public OutMessageBuilder finalMessage(String finalMessage) {
            this.finalMessage = finalMessage;
            return this;
        }

        public OutMessageBuilder messageType(int messageType) {
            this.messageType = messageType;
            return this;
        }

        public OutMessageBuilder message(Message message) {
            this.message = message;
            return this;
        }

        public OutMessageBuilder event(MessageEvent event) {
            if (event instanceof GroupMessageEvent) {
                this.groupId = ((GroupMessageEvent) event).getGroup().getId();
            }
            this.senderId = event.getSender().getId();
            if (event instanceof GroupMessageEvent) {
                this.fromMsgType = AppConstants.MSG_FROM_GROUP;
            } else {
                this.fromMsgType = AppConstants.MSG_FROM_FRIEND;
            }
            return this;
        }

        public OutMessageBuilder groupId(long groupId) {
            this.groupId = groupId;
            return this;
        }

        public OutMessageBuilder senderId(long senderId) {
            this.senderId = senderId;
            return this;
        }

        public OutMessageBuilder fromMsgType(int fromMsgType) {
            this.fromMsgType = fromMsgType;
            return this;
        }


        public OutMessage build() {
            return new OutMessage(content, pluginCode, convertFlag, templateCode, fillFlag, fillMap, fillObj, finalMessage, messageType, message, groupId, senderId, fromMsgType);
        }

        @Override
        public String toString() {
            return "OutMessageBuilder{" +
                    "content='" + content + '\'' +
                    ", pluginCode='" + pluginCode + '\'' +
                    ", convertFlag=" + convertFlag +
                    ", templateCode='" + templateCode + '\'' +
                    ", fillFlag=" + fillFlag +
                    ", fillMap=" + fillMap +
                    ", fillObj=" + fillObj +
                    ", finalMessage='" + finalMessage + '\'' +
                    ", messageType=" + messageType +
                    '}';
        }
    }
}
