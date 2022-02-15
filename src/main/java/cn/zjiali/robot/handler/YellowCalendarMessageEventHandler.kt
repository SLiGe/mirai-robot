package cn.zjiali.robot.handler

import cn.zjiali.robot.constant.AppConstants
import cn.zjiali.robot.constant.PluginCode
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.model.response.RobotBaseResponse
import cn.zjiali.robot.model.response.YellowCalendarResponse
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import cn.zjiali.robot.util.PluginConfigUtil.getApiURL
import com.google.gson.reflect.TypeToken
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent

/**
 * 老黄历消息处理器
 *
 * @author zJiaLi
 * @since 2021-04-04 20:35
 */
class YellowCalendarMessageEventHandler : AbstractMessageEventHandler() {
    override fun handleGroupMessageEvent(event: GroupMessageEvent): OutMessage {
        return yellowCalendarMessage!!
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent): OutMessage {
        return yellowCalendarMessage!!
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String): Boolean {
        return containCommand(PluginCode.YELLOW_CALENDAR, msg)
    }

    /**
     * 获取老黄历消息
     *
     */
    private val yellowCalendarMessage: OutMessage?
        get() {
            val response = HttpUtil.get(getApiURL(PluginCode.YELLOW_CALENDAR))
            val type = object : TypeToken<RobotBaseResponse<YellowCalendarResponse?>?>() {}.type
            val baseResponse = JsonUtil.toObjByType<RobotBaseResponse<YellowCalendarResponse>>(response, type)
            if (baseResponse.status == 200) {
                val yellowCalendarResponse = baseResponse.data
                return OutMessage.builder().convertFlag(true).templateCode(PluginCode.YELLOW_CALENDAR)
                    .pluginCode(PluginCode.YELLOW_CALENDAR)
                    .fillObj(yellowCalendarResponse).fillFlag(AppConstants.FILL_OUT_MESSAGE_OBJECT_FLAG).build()
            }
            return null
        }
}