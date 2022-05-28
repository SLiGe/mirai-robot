package cn.zjiali.robot.model.message;

import net.mamoe.mirai.message.data.Message;

import java.util.Map;
import java.util.Objects;

/**
 * @author zJiaLi
 * @since 2021-09-04 11:03
 */
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

    OutMessage(String content, String pluginCode, boolean convertFlag, String templateCode, int fillFlag, Map<String, String> fillMap, Object fillObj, String finalMessage, int messageType, Message message) {
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
    }

    public static OutMessageBuilder builder() {
        return new OutMessageBuilder();
    }


    public String getContent() {
        return this.content;
    }

    public String getPluginCode() {
        return this.pluginCode;
    }

    public boolean isConvertFlag() {
        return this.convertFlag;
    }

    public String getTemplateCode() {
        return this.templateCode;
    }

    public int getFillFlag() {
        return this.fillFlag;
    }

    public Map<String, String> getFillMap() {
        return this.fillMap;
    }

    public Object getFillObj() {
        return this.fillObj;
    }

    public String getFinalMessage() {
        return this.finalMessage;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPluginCode(String pluginCode) {
        this.pluginCode = pluginCode;
    }

    public void setConvertFlag(boolean convertFlag) {
        this.convertFlag = convertFlag;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public void setFillFlag(int fillFlag) {
        this.fillFlag = fillFlag;
    }

    public void setFillMap(Map<String, String> fillMap) {
        this.fillMap = fillMap;
    }

    public void setFillObj(Object fillObj) {
        this.fillObj = fillObj;
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

    @Override
    public String toString() {
        return "OutMessage{" +
                "content='" + content + '\'' +
                ", pluginCode='" + pluginCode + '\'' +
                ", convertFlag=" + convertFlag +
                ", templateCode='" + templateCode + '\'' +
                ", fillFlag=" + fillFlag +
                ", fillMap=" + fillMap +
                ", fillObj=" + fillObj +
                ", finalMessage='" + finalMessage + '\'' +
                ", message=" + message +
                ", messageType=" + messageType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutMessage that = (OutMessage) o;
        return convertFlag == that.convertFlag && fillFlag == that.fillFlag && messageType == that.messageType && Objects.equals(content, that.content) && Objects.equals(pluginCode, that.pluginCode) && Objects.equals(templateCode, that.templateCode) && Objects.equals(fillMap, that.fillMap) && Objects.equals(fillObj, that.fillObj) && Objects.equals(finalMessage, that.finalMessage) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, pluginCode, convertFlag, templateCode, fillFlag, fillMap, fillObj, finalMessage, message, messageType);
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


        public OutMessage build() {
            return new OutMessage(content, pluginCode, convertFlag, templateCode, fillFlag, fillMap, fillObj, finalMessage, messageType, message);
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
