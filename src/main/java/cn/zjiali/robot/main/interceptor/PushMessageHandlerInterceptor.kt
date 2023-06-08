package cn.zjiali.robot.main.interceptor

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.Constants
import cn.zjiali.robot.constant.MsgType
import cn.zjiali.robot.main.websocket.WebSocketManager
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.util.JsonUtil
import com.google.inject.Inject
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.anyIsInstance

/**
 * @author zJiaLi
 * @since 2022-12-01 16:37
 */
class PushMessageHandlerInterceptor : HandlerInterceptor {

    private val logger: CommonLogger = CommonLogger(javaClass)

    @Inject
    private val webSocketManager: WebSocketManager? = null
    override fun preHandle(messageEvent: MessageEvent?): Boolean {
        if (messageEvent is FriendMessageEvent || messageEvent is GroupMessageEvent) {
            val param = HashMap<String, Any>()
            param["robot"] = AppConfig.getQQ()
            val messageBody = HashMap<String, Any>()
            val message = messageEvent.message
            if (messageEvent is FriendMessageEvent) {
                param["msgType"] = MsgType.FRIEND_MSG
                messageBody["content"] = message.contentToString()
            } else if (messageEvent is GroupMessageEvent) {
                messageBody["content"] = message.contentToString()
                param["msgType"] = MsgType.GROUP_MSG
                messageBody["group"] = messageEvent.group.id
                messageBody["groupName"] = messageEvent.group.name
                if (message.anyIsInstance<At>()) {
                    val atList = ArrayList<Map<Long, String>>()
                    message.filterIsInstance<At>().forEach { at ->
                        run {
                            val atInfo = HashMap<Long, String>()
                            atInfo[at.target] = at.getDisplay(messageEvent.group)
                            atList.add(atInfo)
                        }
                    }
                    messageBody["atList"] = atList
                    val at = message.filterIsInstance<At>().first()
                    if (at.target == AppConfig.qq()) {
                        messageBody["atFlag"] = Constants.Y
                        messageBody["content"] = message.filterNot { it is At }.map { it.contentToString() }
                            .reduce { s1, s2 -> s1 + s2 }
                    }
                }
            }
            messageBody["qq"] = messageEvent.sender.id
            messageBody["nickName"] = messageEvent.sender.nick
            param["messageBody"] = messageBody
            val requestJson = JsonUtil.obj2str(param)
            webSocketManager!!.sendText(requestJson)
            logger.info("send websocket:{}", requestJson)
        }
        return super.preHandle(messageEvent)
    }
}