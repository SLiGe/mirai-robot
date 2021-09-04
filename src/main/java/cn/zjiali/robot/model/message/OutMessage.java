package cn.zjiali.robot.model.message;

import java.util.Map;

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

    public OutMessage(String content, boolean convertFlag, String templateCode, int fillFlag, Map<String, String> fillMap, Object fillObj, String finalMessage) {
        this.content = content;
        this.convertFlag = convertFlag;
        this.templateCode = templateCode;
        this.fillFlag = fillFlag;
        this.fillMap = fillMap;
        this.fillObj = fillObj;
        this.finalMessage = finalMessage;
    }

    public static OutMessageBuilder builder() {
        return new OutMessageBuilder();
    }

    public OutMessageBuilder toBuilder() {
        return new OutMessageBuilder().content(this.content).convertFlag(this.convertFlag).templateCode(this.templateCode).fillFlag(this.fillFlag).fillMap(this.fillMap).fillObj(this.fillObj);
    }

    public String getContent() {
        return this.content;
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OutMessage)) return false;
        final OutMessage other = (OutMessage) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) return false;
        if (this.isConvertFlag() != other.isConvertFlag()) return false;
        final Object this$templateCode = this.getTemplateCode();
        final Object other$templateCode = other.getTemplateCode();
        if (this$templateCode == null ? other$templateCode != null : !this$templateCode.equals(other$templateCode))
            return false;
        if (this.getFillFlag() != other.getFillFlag()) return false;
        final Object this$fillMap = this.getFillMap();
        final Object other$fillMap = other.getFillMap();
        if (this$fillMap == null ? other$fillMap != null : !this$fillMap.equals(other$fillMap)) return false;
        final Object this$fillObj = this.getFillObj();
        final Object other$fillObj = other.getFillObj();
        if (this$fillObj == null ? other$fillObj != null : !this$fillObj.equals(other$fillObj)) return false;
        final Object this$finalMessage = this.getFinalMessage();
        final Object other$finalMessage = other.getFinalMessage();
        if (this$finalMessage == null ? other$finalMessage != null : !this$finalMessage.equals(other$finalMessage))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof OutMessage;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        result = result * PRIME + (this.isConvertFlag() ? 79 : 97);
        final Object $templateCode = this.getTemplateCode();
        result = result * PRIME + ($templateCode == null ? 43 : $templateCode.hashCode());
        result = result * PRIME + this.getFillFlag();
        final Object $fillMap = this.getFillMap();
        result = result * PRIME + ($fillMap == null ? 43 : $fillMap.hashCode());
        final Object $fillObj = this.getFillObj();
        result = result * PRIME + ($fillObj == null ? 43 : $fillObj.hashCode());
        final Object $finalMessage = this.getFinalMessage();
        result = result * PRIME + ($finalMessage == null ? 43 : $finalMessage.hashCode());
        return result;
    }

    public String toString() {
        return "OutMessage(content=" + this.getContent() + ", convertFlag=" + this.isConvertFlag() + ", templateCode=" + this.getTemplateCode() + ", fillFlag=" + this.getFillFlag() + ", fillMap=" + this.getFillMap() + ", fillObj=" + this.getFillObj() + ", finalMessage=" + this.getFinalMessage() + ")";
    }


    public static class OutMessageBuilder {
        private String content;
        private boolean convertFlag;
        private String templateCode;
        private int fillFlag;
        private Map<String, String> fillMap;
        private Object fillObj;
        private String finalMessage;

        OutMessageBuilder() {
        }

        public OutMessageBuilder content(String content) {
            this.content = content;
            return this;
        }

        public OutMessageBuilder finalMessage(String finalMessage) {
            this.finalMessage = finalMessage;
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

        public OutMessage build() {
            return new OutMessage(content, convertFlag, templateCode, fillFlag, fillMap, fillObj, finalMessage);
        }

        public String toString() {
            return "OutMessage.OutMessageBuilder(content=" + this.content + ", convertFlag=" + this.convertFlag + ", templateCode=" + this.templateCode + ", fillFlag=" + this.fillFlag + ", fillMap=" + this.fillMap + ", fillObj=" + this.fillObj + ")";
        }
    }
}
