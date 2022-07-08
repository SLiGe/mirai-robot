package cn.zjiali.robot.handler

import cn.hutool.core.collection.CollectionUtil
import cn.hutool.core.exceptions.ExceptionUtil
import cn.zjiali.robot.constant.ApiUrl
import cn.zjiali.robot.constant.AppConstants
import cn.zjiali.robot.main.OutMessageConvert.Companion.instance
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.model.response.RobotBaseResponse
import cn.zjiali.robot.model.server.PluginInfo
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import cn.zjiali.robot.util.ObjectUtil
import cn.zjiali.robot.util.PropertiesUtil
import com.google.common.collect.Lists
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
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
import okhttp3.internal.filterList
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
    @Inject
    private val messageEventHandlers: Set<MessageEventHandler>? = null
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

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
        isGroup: Boolean,
        groupMessageEvent: GroupMessageEvent?,
        friendMessageEvent: MessageEvent?
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
                    if (outMessage == null) continue
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
                    if (outMessage == null) continue
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
        isGroup: Boolean,
        groupMessageEvent: GroupMessageEvent?
    ): List<MessageEventHandler> {
        var messageEventHandlerList = messageEventHandlers!!.toList()
        if (isGroup) {
            val groupNumber = groupMessageEvent!!.group.id
            val jsonObject = JsonObject()
            jsonObject.addProperty("groupNumber", groupNumber)
            val serverGroupPluginJson =
                HttpUtil.post(PropertiesUtil.getApiProperty(ApiUrl.QUERY_GROUP_PLUGIN), JsonUtil.obj2str(jsonObject))
            logger.debug("群组插件配置:{}", serverGroupPluginJson)
            val pluginInfoResponse = JsonUtil.toObjByType<RobotBaseResponse<List<PluginInfo?>>>(
                serverGroupPluginJson,
                object : TypeToken<RobotBaseResponse<List<PluginInfo?>?>?>() {}.type
            )
            val serverGroupPluginList = pluginInfoResponse.data
            if (CollectionUtil.isNotEmpty(serverGroupPluginList)) {
                val unActivePluginCodeSet =
                    serverGroupPluginList.stream().filter { plugin -> plugin?.activeFlag == "0" }
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