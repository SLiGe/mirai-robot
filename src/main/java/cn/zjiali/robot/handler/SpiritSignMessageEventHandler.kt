package cn.zjiali.robot.handler

import cn.hutool.core.bean.copier.BeanCopier
import cn.hutool.core.bean.copier.CopyOptions
import cn.zjiali.robot.constant.AppConstants
import cn.zjiali.robot.constant.MsgTemplate
import cn.zjiali.robot.constant.PluginCode
import cn.zjiali.robot.constant.PluginProperty
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.model.response.RobotBaseResponse
import cn.zjiali.robot.model.response.SignPerDayRes
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import com.google.common.collect.Maps
import com.google.gson.reflect.TypeToken
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent

/**
 *
 * @author zJiaLi
 * @since 2022-01-31 22:52
 */
class SpiritSignMessageEventHandler : AbstractMessageEventHandler() {

    override fun handleGroupMessageEvent(event: GroupMessageEvent?): OutMessage? {
        return signPerDay(getMsg(event), event?.sender?.id.toString(), event!!.group.id, AppConstants.MSG_FROM_GROUP)
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent?): OutMessage? {
        return signPerDay(getMsg(event), event?.sender?.id.toString(), 0L, AppConstants.MSG_FROM_FRIEND)
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String?): Boolean {
        return containCommand(PluginCode.LQ, msg)
    }

    override fun matchCommand(messageEvent: MessageEvent?): Boolean {
        return containCommand(PluginCode.LQ, messageEvent)
    }

    override fun code(): String {
        return PluginCode.LQ
    }

    fun signPerDay(msg: String, qq: String, groupId: Long?, msgFrom: Int): OutMessage? {
        val signType = chooseSignType(msg)
        val paramMap = Maps.newHashMap<String, Any>()
        if (signType != null) {
            paramMap["type"] = signType
        }
        paramMap["qq"] = qq
        val signRes = HttpUtil.post(getConfigVal(PluginCode.LQ, PluginProperty.API_URL, groupId, qq.toLong()), paramMap)
        if (signRes != null) {
            val type = object : TypeToken<RobotBaseResponse<SignPerDayRes?>?>() {}.type
            val robotBaseResponse = JsonUtil.toObjByType<RobotBaseResponse<SignPerDayRes?>?>(signRes, type)
            if (robotBaseResponse.success()) {
                val signData = robotBaseResponse.data?.signData
                val messageParamMap = Maps.newHashMap<String, String>()
                BeanCopier.create<Map<String, String>>(
                    signData, messageParamMap,
                    CopyOptions.create()
                        .setIgnoreNullValue(false)
                        .setFieldNameEditor { key: String? -> key }
                ).copy()
                messageParamMap["viewUrl"] = robotBaseResponse.data?.viewUrl.toString()
                return OutMessage.builder().convertFlag(true).templateCode(chooseSignTemplate(msg))
                    .pluginCode(PluginCode.LQ)
                    .groupId(groupId!!)
                    .fromMsgType(msgFrom)
                    .fillMap(messageParamMap).fillFlag(AppConstants.FILL_OUT_MESSAGE_MAP_FLAG).build()
            }
        }
        return null
    }

    private fun chooseSignTemplate(msg: String): String? {
        return if (msg.contains("月老灵签"))
            MsgTemplate.YL_LQ_TEMPLATE
        else if (msg.contains("财神灵签"))
            MsgTemplate.CS_LQ_TEMPLATE
        else if (msg.contains("观音灵签"))
            MsgTemplate.GY_LQ_TEMPLATE
        else null
    }

    private fun chooseSignType(msg: String): String? {
        return if (msg.contains("月老灵签"))
            "yllq"
        else if (msg.contains("财神灵签"))
            "cslq"
        else if (msg.contains("观音灵签"))
            "gylq"
        else null
    }
}