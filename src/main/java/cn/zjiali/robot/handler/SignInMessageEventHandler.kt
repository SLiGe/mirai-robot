package cn.zjiali.robot.handler

import cn.zjiali.robot.constant.AppConstants
import cn.zjiali.robot.constant.MsgTemplate
import cn.zjiali.robot.constant.PluginCode
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.service.SignInService
import com.google.inject.Inject
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent

/**
 * 签到处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 20:57
 */
class SignInMessageEventHandler : AbstractMessageEventHandler() {
    @Inject
    private val signInService: SignInService? = null


    override fun handleGroupMessageEvent(event: GroupMessageEvent): OutMessage {
        val message = event.message.contentToString()
        val senderQQ = event.sender.id
        val groupNum = event.group.id
        return getSignInMsg(message, senderQQ, groupNum, 2, event)!!
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent): OutMessage {
        val message = event.message.contentToString()
        val senderQQ = event.sender.id
        return getSignInMsg(message, senderQQ, 0, 1, event)!!
    }

    private fun getSignInMsg(
        message: String,
        senderQQ: Long,
        groupNum: Long,
        msgType: Int,
        event: MessageEvent
    ): OutMessage? {
        if (message.contains("签到")) {
            val response =
                signInService!!.doSignIn(senderQQ.toString(), groupNum.toString(), msgType)
                    ?: return OutMessage.builder().pluginCode(PluginCode.SIGN).convertFlag(false)
                        .content("签到服务异常,请联系管理员!")
                        .build()
            val status = response.status!!
            //签到成功200  已经签到203
            if (status == 200) {
                val dataResponse = response.dataResponse
                return OutMessage.builder().pluginCode(PluginCode.SIGN).convertFlag(true)
                    .fillFlag(AppConstants.FILL_OUT_MESSAGE_OBJECT_FLAG).event(event)
                    .templateCode(MsgTemplate.SIGN_TEMPLATE).fillObj(dataResponse).build()
            } else if (status == 203) {
                return OutMessage.builder().pluginCode(PluginCode.SIGN)
                    .convertFlag(false).content("你今天已经签到过了！").build()
            }
        } else if (message.contains("积分查询")) {
            val response =
                signInService!!.getSignInData(senderQQ.toString(), groupNum.toString(), 2)
                    ?: return OutMessage.builder().pluginCode(PluginCode.SIGN).convertFlag(false)
                        .content("签到服务异常,请联系管理员!")
                        .build()
            val dataResponse = response.dataResponse
            return OutMessage.builder().convertFlag(true)
                .pluginCode(PluginCode.SIGN)
                .event(event)
                .fillFlag(AppConstants.FILL_OUT_MESSAGE_OBJECT_FLAG)
                .templateCode(MsgTemplate.QUERY_SIGN_TEMPLATE).fillObj(dataResponse).build()
        }
        return null
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String): Boolean {
        return containCommand(PluginCode.SIGN, msg)
    }

    override fun matchCommand(messageEvent: MessageEvent?): Boolean {
        return containCommand(PluginCode.SIGN, messageEvent)
    }

    override fun code(): String {
        return PluginCode.SIGN
    }
}