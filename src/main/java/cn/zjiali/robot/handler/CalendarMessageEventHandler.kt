package cn.zjiali.robot.handler

import cn.zjiali.robot.constant.AppConstants
import cn.zjiali.robot.constant.PluginCode
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.model.response.CalendarResponse
import cn.zjiali.robot.model.response.RobotBaseResponse
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import cn.zjiali.robot.util.PluginConfigUtil.getApiURL
import com.google.gson.reflect.TypeToken
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent

/**
 * 万年历处理器
 *
 * @author zJiaLi
 * @since 2021-04-04 11:02
 */
class CalendarMessageEventHandler : AbstractMessageEventHandler() {
    override fun handleGroupMessageEvent(event: GroupMessageEvent): OutMessage {
        return calendarMessage!!
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent): OutMessage {
        return calendarMessage!!
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String): Boolean {
        return containCommand(PluginCode.CALENDAR, msg)
    }

    companion object {
        /**
         * 获取万年历消息
         *
         * @return
         */
        val calendarMessage: OutMessage?
            get() {
                val response = HttpUtil.get(getApiURL(PluginCode.CALENDAR))
                val type = object : TypeToken<RobotBaseResponse<CalendarResponse?>?>() {}.type
                val baseResponse = JsonUtil.toObjByType<RobotBaseResponse<CalendarResponse>>(response, type)
                if (baseResponse.status == 200) {
                    val calendarResponse = baseResponse.data
                    return OutMessage.builder().convertFlag(true).fillFlag(AppConstants.FILL_OUT_MESSAGE_OBJECT_FLAG)
                        .pluginCode(PluginCode.CALENDAR)
                        .fillObj(calendarResponse).templateCode(PluginCode.CALENDAR).build()
                }
                return null
            }
    }
}