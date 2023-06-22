package cn.zjiali.robot.main.interceptor

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.Constants
import cn.zjiali.robot.constant.MsgType
import cn.zjiali.robot.main.websocket.WebSocketManager
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.util.JsonUtil
import com.google.inject.Inject
import kotlinx.coroutines.*
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl

/**
 * @author zJiaLi
 * @since 2022-12-01 16:37
 */
class PushMessageHandlerInterceptor( private val dispatcher: CoroutineDispatcher = Dispatchers.Default) : HandlerInterceptor {

    private val logger: CommonLogger = CommonLogger(javaClass)

    @Inject
    private val webSocketManager: WebSocketManager? = null

    data class ImageInfo(
        val imageId: String,
        val size: Long,
        val imageType: ImageType,
        val width: Int,
        val height: Int,
        val isEmoji: Boolean,
        val url: String
    )

    override fun preHandle(messageEvent: MessageEvent?): Boolean {
        if (messageEvent is FriendMessageEvent || messageEvent is GroupMessageEvent) {
            val param = HashMap<String, Any>()
            param["robot"] = AppConfig.getQQ()
            val messageBody = HashMap<String, Any>()
            val message = messageEvent.message
            val msgContentType:String
            val singleMessageList = message.filterNot { it is MessageSource }
            msgContentType = if (singleMessageList.size == 1) {
                checkMsgContentType(singleMessageList.first())
            } else {
                "multi"
            }
            val messageList = mapMessageList(message)
            messageBody["messageList"] = messageList
            messageBody["msgContentType"] = msgContentType
            if (messageEvent is FriendMessageEvent) {
                param["msgType"] = MsgType.FRIEND_MSG
                messageBody["content"] = message.contentToString()
            } else if (messageEvent is GroupMessageEvent) {
                messageBody["content"] = message.contentToString()
                param["msgType"] = MsgType.GROUP_MSG
                messageBody["group"] = messageEvent.group.id
                messageBody["groupName"] = messageEvent.group.name
                if (message.anyIsInstance<At>()) {
                    val atList = ArrayList<Map<String, Any>>()
                    message.filterIsInstance<At>().forEach { at ->
                        run {
                            val atInfo = HashMap<String, Any>()
                            atInfo["qq"] = at.target
                            atInfo["nickName"] = at.getDisplay(messageEvent.group).drop(1)
                            atList.add(atInfo)
                        }
                    }
                    messageBody["atList"] = atList
                    val at = message.filterIsInstance<At>().first()
                    if (at.target == AppConfig.qq()) {
                        messageBody["atFlag"] = Constants.Y
                    }
                    messageBody["content"] = message.filterNot { it is At }.map { it.contentToString() }
                        .reduce { s1, s2 -> s1 + s2 }
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun mapMessageList(message: MessageChain): ArrayList<Map<String, Any>> {
        val messageList = ArrayList<Map<String, Any>>()
        for (singleMessage in message) {
            val itMsgContentType = checkMsgContentType(singleMessage)
            val msgInfo = HashMap<String, Any>()
            msgInfo["type"] = itMsgContentType
            if (singleMessage is MessageSource) {
                continue
            }
            if (itMsgContentType == "image" && singleMessage is Image) {
                GlobalScope.launch(dispatcher) {
                    msgInfo["imageInfo"] = ImageInfo(
                        singleMessage.imageId,
                        singleMessage.size,
                        singleMessage.imageType,
                        singleMessage.width,
                        singleMessage.height,
                        singleMessage.isEmoji,
                        singleMessage.queryUrl()
                    )
                }
            }
            msgInfo["content"] = singleMessage.content
            messageList.add(msgInfo)
        }
        return messageList
    }

    private fun checkMsgContentType(singleMessage: SingleMessage): String {
        var msgContentType = ""
        when (singleMessage) {
            is PlainText -> {
                msgContentType = "text"
            }

            is Image -> {
                msgContentType = "image"
            }

            is At -> {
                msgContentType = "at"
            }
        }
        return msgContentType;
    }
}