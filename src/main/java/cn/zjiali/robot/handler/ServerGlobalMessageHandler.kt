package cn.zjiali.robot.handler

import cn.hutool.core.collection.CollectionUtil
import cn.hutool.core.exceptions.ExceptionUtil
import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.AppConstants
import cn.zjiali.robot.main.OutMessageConvert.Companion.instance
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.util.ObjectUtil
import cn.zjiali.server.grpc.api.group.GetPluginRequest
import cn.zjiali.server.grpc.api.group.GroupPluginGrpc.GroupPluginBlockingStub
import com.google.common.collect.Lists
import com.google.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.contact.isBotMuted
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.At
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Collectors

/**
 * 服务端消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 21:20
 */
class ServerGlobalMessageHandler : GlobalMessageHandler {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Inject
    private val groupPluginBlockingStub: GroupPluginBlockingStub? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun handleGroupMessageEvent(event: GroupMessageEvent) {
        GlobalScope.launch(Dispatchers.Unconfined) {
            try {
                handleMessage(true, event, null)
            } catch (e: Exception) {
                logger.error("处理群组消息异常,e:{}", ExceptionUtil.stacktraceToString(e))
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun handleFriendMessageEvent(event: FriendMessageEvent) {
        GlobalScope.launch(Dispatchers.Unconfined) {
            try {
                handleMessage(false, null, event)
            } catch (e: Exception) {
                logger.error("处理好友消息异常,e:{}", ExceptionUtil.stacktraceToString(e))
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun handleOtherMessageEvent(event: MessageEvent) {
        GlobalScope.launch(Dispatchers.Unconfined) {
            try {
                handleMessage(false, null, event)
            } catch (e: Exception) {
                logger.error("处理其他消息异常,e:{}", ExceptionUtil.stacktraceToString(e))
            }
        }

    }

    private suspend fun handleMessage(
        isGroup: Boolean, groupMessageEvent: GroupMessageEvent?, friendMessageEvent: MessageEvent?
    ) {
        if (isGroup && groupMessageEvent!!.group.isBotMuted) {
            return
        }
        val preHandle = doPreHandle(if (isGroup) groupMessageEvent else friendMessageEvent)
        if (!preHandle) return
        val messageEventHandlerList = filterMessageEventHandlerList(isGroup, groupMessageEvent)
        val outMessageList: MutableList<OutMessage> = Lists.newLinkedList()
        val messageEvent = if (isGroup) groupMessageEvent else friendMessageEvent
        for (messageEventHandler in messageEventHandlerList) {
            if (messageEventHandler.matchCommand(messageEvent) && !messageEventHandler.ignore(messageEvent)) {
                var outMessage: OutMessage?
                if (isGroup) {
                    outMessage = messageEventHandler.handleGroupMessageEvent(groupMessageEvent)
                    if (outMessage == null || outMessage.isEmpty) continue
                    val messageType = outMessage.messageType
                    if (messageType == AppConstants.MESSAGE_TYPE_HANDLER) {
                        val message = instance.convert(outMessage)
                        if (!ObjectUtil.isNullOrEmpty(message)) groupMessageEvent!!.group.sendMessage(
                            At(
                                groupMessageEvent.sender.id
                            ).plus(message!!)
                        )
                    } else if (messageType == AppConstants.MESSAGE_TYPE_PLUGIN) {
                        if (outMessage.message != null) {
                            groupMessageEvent!!.group.sendMessage(outMessage.message)
                        }
                    }
                } else {
                    outMessage = messageEventHandler.handleFriendMessageEvent(friendMessageEvent as FriendMessageEvent?)
                    if (outMessage == null || outMessage.isEmpty) continue
                    val messageType = outMessage.messageType
                    if (messageType == AppConstants.MESSAGE_TYPE_HANDLER) {
                        val message = instance.convert(outMessage)
                        if (!ObjectUtil.isNullOrEmpty(message)) friendMessageEvent!!.sender.sendMessage(
                            message!!
                        )
                    } else if (messageType == AppConstants.MESSAGE_TYPE_PLUGIN) {
                        if (outMessage.message != null) {
                            friendMessageEvent!!.sender.sendMessage(outMessage.message)
                        }
                    }
                }
                if (ObjectUtil.isNotNullOrEmpty(outMessage)) {
                    outMessageList.add(outMessage)
                }
                if (!messageEventHandler.next()) break
            }
        }
        triggerAfterCompletion(if (isGroup) groupMessageEvent else friendMessageEvent, outMessageList)
    }

    /**
     * 过滤消息处理器
     *
     * @param isGroup 是否群
     * @return 消息处理器
     */
    private fun filterMessageEventHandlerList(
        isGroup: Boolean, groupMessageEvent: GroupMessageEvent?
    ): List<MessageEventHandler> {
        var messageEventHandlerList = AppConfig.msgMessageEventHandlers
        if (isGroup) {
            val groupNumber = groupMessageEvent!!.group.id
            val pluginInfoResponse =
                groupPluginBlockingStub!!.getPlugin(GetPluginRequest.newBuilder().setGroupNumber(groupNumber).build())
            logger.debug("群组插件配置:{}", pluginInfoResponse)
            val serverGroupPluginList = pluginInfoResponse.pluginList
            if (CollectionUtil.isNotEmpty(serverGroupPluginList)) {
                val unActivePluginCodeSet =
                    serverGroupPluginList!!.stream().filter { plugin -> plugin?.pluginStatus == 0 }
                        .map { plugin -> plugin?.pluginCode }.collect(Collectors.toSet())
                messageEventHandlerList = messageEventHandlerList.filter { messageEventHandler: MessageEventHandler ->
                    val code = messageEventHandler.code()
                    !unActivePluginCodeSet.contains(code)
                }
            }
        }
        //处理器排序
        messageEventHandlerList.sortedWith(Comparator.comparingInt { obj: MessageEventHandler -> obj.order() })
        return messageEventHandlerList
    }
}