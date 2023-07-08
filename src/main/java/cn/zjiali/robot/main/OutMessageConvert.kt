package cn.zjiali.robot.main

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.AppConstants
import cn.zjiali.robot.manager.PluginManager
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.util.GuiceUtil
import cn.zjiali.robot.util.MessageUtil
import cn.zjiali.robot.util.PluginConfigUtil

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
        val pluginManager = GuiceUtil.getBean(PluginManager::class.java)
        val template =
            if (outMessage.fromMsgType == AppConstants.MSG_FROM_GROUP && AppConfig.serverControl())
                pluginManager.getTemplate(outMessage.pluginCode, templateCode, outMessage.groupId, outMessage.senderId)
            else PluginConfigUtil.getTemplate(outMessage.pluginCode, templateCode)
        val fillFlag = outMessage.fillFlag
        val finalMessage = if (fillFlag == AppConstants.FILL_OUT_MESSAGE_OBJECT_FLAG) template.let {
            MessageUtil.replaceMessage(
                it,
                outMessage.fillObj
            )
        }
        else template.let { MessageUtil.replaceMessageByMap(it, outMessage.fillMap) }
        outMessage.finalMessage = finalMessage
        return finalMessage
    }

    companion object {
        @JvmStatic
        val instance = OutMessageConvert()
    }
}