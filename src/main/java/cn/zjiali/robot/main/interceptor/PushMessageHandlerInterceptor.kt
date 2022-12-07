package cn.zjiali.robot.main.interceptor

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.main.websocket.WebSocketManager
import cn.zjiali.robot.manager.WsSecurityManager
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.util.JsonUtil
import com.google.inject.Inject
import com.google.inject.name.Named
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.At

/**
 * @author zJiaLi
 * @since 2022-12-01 16:37
 */
class PushMessageHandlerInterceptor : HandlerInterceptor {
    @Inject
    @Named("DefaultWsSecurityManager")
    private val wsSecurityManager: WsSecurityManager? = null

    private val logger: CommonLogger = CommonLogger(javaClass)

    @Inject
    private val webSocketManager: WebSocketManager? = null
    override fun preHandle(messageEvent: MessageEvent?): Boolean {
        if (messageEvent is FriendMessageEvent || messageEvent is GroupMessageEvent) {
            val param = HashMap<String, Any>()
            param["robot"] = AppConfig.getQQ()
            val messageBody = HashMap<String, Any>()
            if (messageEvent is FriendMessageEvent) {
                param["msgType"] = 1
                messageBody["content"] = messageEvent.message.contentToString()
            } else if (messageEvent is GroupMessageEvent) {
                messageBody["content"] = messageEvent.message.contentToString()
                param["msgType"] = 2
                messageBody["group"] = messageEvent.group.id
                messageBody["groupName"] = messageEvent.group.name
                for (singleMessage in messageEvent.message) {
                    if (singleMessage is At) {
                        messageBody["atFlag"] = "1"
                        messageBody["content"] = singleMessage.contentToString()
                    }
                }
            }
            messageBody["qq"] = messageEvent.sender.id
            messageBody["nickName"] = messageEvent.sender.nick
            param["messageBody"] = messageBody
            val requestJson = JsonUtil.obj2str(param)
            val session = webSocketManager?.getSession(AppConfig.getQQ())
            session!!.send(wsSecurityManager!!.encryptMsgData(requestJson))
            logger.debug("send websocket:{}", requestJson)
        }
        return super.preHandle(messageEvent)
    }
}