package cn.zjiali.robot.main

import cn.zjiali.robot.constant.AppConstants
import cn.zjiali.robot.util.PluginConfigUtil.getTemplate
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.util.MessageUtil

/**
 * 发送消息转换类
 *
 * @author zJiaLi
 * @since 2021-09-04 11:26
 */
class OutMessageConvert {

    fun convert(outMessage: OutMessage?): String? {
        if (outMessage == null) return null
        val content = outMessage.content
        if (!outMessage.isConvertFlag) {
            outMessage.finalMessage = content
            return content
        }
        val templateCode = outMessage.templateCode
        val template = getTemplate(outMessage.pluginCode, templateCode)
        val fillFlag = outMessage.fillFlag
        val finalMessage = if (fillFlag == AppConstants.FILL_OUT_MESSAGE_OBJECT_FLAG) MessageUtil.replaceMessage(
            template,
            outMessage.fillObj
        )
        else MessageUtil.replaceMessageByMap(template, outMessage.fillMap)
        outMessage.finalMessage = finalMessage
        return finalMessage
    }

    companion object {
        @JvmStatic
        val instance = OutMessageConvert()
    }
}