package cn.zjiali.robot.main;

import cn.zjiali.robot.util.PluginConfigUtil;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.util.MessageUtil;

/**
 * 发送消息转换类
 *
 * @author zJiaLi
 * @since 2021-09-04 11:26
 */
public class OutMessageConvert {

    private static final OutMessageConvert instance = new OutMessageConvert();

    public static OutMessageConvert getInstance() {
        return instance;
    }

    public String convert(OutMessage outMessage) {
        if (outMessage == null) return null;
        String content = outMessage.getContent();
        if (!outMessage.isConvertFlag()) return content;
        String templateCode = outMessage.getTemplateCode();
        String template = PluginConfigUtil.getTemplate(templateCode);
        int fillFlag = outMessage.getFillFlag();
        String finalMessage = MessageUtil.replaceMessage(template, fillFlag == 1 ? outMessage.getFillMap() : outMessage.getFillObj());
        outMessage.setFinalMessage(finalMessage);
        return finalMessage;
    }

}
