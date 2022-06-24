package cn.zjiali.robot.handler

import cn.hutool.core.collection.CollectionUtil
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
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.At
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

    @OptIn(DelicateCoroutinesApi::class)
    override fun handleGroupMessageEvent(event: GroupMessageEvent) {
        GlobalScope.launch(Dispatchers.Unconfined) {
            handleMessage(true, event, null)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun handleFriendMessageEvent(event: FriendMessageEvent) {
        GlobalScope.launch(Dispatchers.Unconfined) {
            handleMessage(false, null, event)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun handleOtherMessageEvent(event: MessageEvent) {
        GlobalScope.launch(Dispatchers.Unconfined) {
            handleMessage(false, null, event)
        }

    }

    private suspend fun handleMessage(
        isGroup: Boolean,
        groupMessageEvent: GroupMessageEvent?,
        friendMessageEvent: MessageEvent?
    ) {
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
        val messageEventHandlerList = messageEventHandlers!!.toList()
        if (isGroup) {
            val groupNumber = groupMessageEvent!!.group.id
            val jsonObject = JsonObject()
            jsonObject.addProperty("groupNumber", groupNumber)
            val serverGroupPluginJson =
                HttpUtil.post(PropertiesUtil.getApiProperty(ApiUrl.QUERY_GROUP_PLUGIN), JsonUtil.obj2str(jsonObject))
            val pluginInfoResponse = JsonUtil.toObjByType<RobotBaseResponse<List<PluginInfo?>>>(
                serverGroupPluginJson,
                object : TypeToken<RobotBaseResponse<List<PluginInfo?>?>?>() {}.type
            )
            val serverGroupPluginList = pluginInfoResponse.data
            if (CollectionUtil.isNotEmpty(serverGroupPluginList)) {
                val pluginCodeSet =
                    serverGroupPluginList.stream().map { plugin -> plugin!!.pluginCode }.collect(Collectors.toSet())
                messageEventHandlerList.dropWhile { messageEventHandler: MessageEventHandler ->
                    val code = messageEventHandler.code()
                    !pluginCodeSet.contains(code)
                }
            }
        }
        //处理器排序
        messageEventHandlerList.sortedWith(Comparator.comparingInt { obj: MessageEventHandler -> obj.order() })
        return messageEventHandlerList
    }
}