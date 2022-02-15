package cn.zjiali.robot.handler

import cn.zjiali.robot.constant.PluginCode
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.model.response.RobotBaseResponse
import cn.zjiali.robot.model.response.TodayOnHistoryResponse
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import cn.zjiali.robot.util.ObjectUtil
import cn.zjiali.robot.util.PluginConfigUtil.getApiURL
import com.google.gson.reflect.TypeToken
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent

/**
 * 历史上的今天
 *
 * @author zJiaLi
 * @since 2021-03-21 11:33
 */
class TodayOfHistoryMessageEventHandler : AbstractMessageEventHandler() {
    override fun handleGroupMessageEvent(event: GroupMessageEvent): OutMessage {
        return OutMessage.builder().pluginCode(PluginCode.TODAY_HISTORY).convertFlag(false).content(
            todayOnHistoryMessage
        ).build()
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent): OutMessage {
        return OutMessage.builder().pluginCode(PluginCode.TODAY_HISTORY).convertFlag(false).content(
            todayOnHistoryMessage
        ).build()
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String): Boolean {
        return containCommand(PluginCode.TODAY_HISTORY, msg)
    }

    /**
     * 获取历史上的今天消息
     */
    private val todayOnHistoryMessage: String?
        get() {
            val response = HttpUtil.get(getApiURL(PluginCode.TODAY_HISTORY))
            val todayOnHistoryResponse = JsonUtil.toObjByType<RobotBaseResponse<List<TodayOnHistoryResponse>>>(
                response,
                object : TypeToken<RobotBaseResponse<List<TodayOnHistoryResponse?>?>?>() {}.type
            )
            if (todayOnHistoryResponse != null) {
                if (todayOnHistoryResponse.status == 200) {
                    val todayOnHistoryResponseResult = todayOnHistoryResponse.data
                    if (!ObjectUtil.isNullOrEmpty(todayOnHistoryResponseResult)) {
                        val stringBuilder = StringBuilder()
                        for (item in todayOnHistoryResponseResult) {
                            stringBuilder.append(item.happenDate).append("\n").append(item.title)
                        }
                        return stringBuilder.toString()
                    }
                }
            }
            return null
        }
}