package cn.zjiali.robot.handler

import cn.zjiali.robot.constant.AppConstants
import cn.zjiali.robot.constant.PluginCode
import cn.zjiali.robot.constant.PluginProperty
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.model.response.FortuneResponse
import cn.zjiali.robot.model.response.RobotBaseResponse
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import cn.zjiali.robot.util.PluginConfigUtil.getApiURL
import cn.zjiali.robot.util.PluginConfigUtil.getConfigVal
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent

/**
 * 运势消息处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 20:58
 */
class FortuneMessageEventHandler : AbstractMessageEventHandler() {


    /**
     * 获取运势消息
     *
     * @param senderQQ 发送人QQ
     * @param groupNum 群组
     * @param msgType  消息类型
     * @return
     */
    private fun getFortuneMsg(senderQQ: Long, groupNum: Long, msgType: Int): OutMessage? {
        val jsonObject = JsonObject()
        jsonObject.addProperty("qq", senderQQ.toString())
        jsonObject.addProperty("isOne", getConfigVal(PluginCode.FORTUNE, PluginProperty.FORTUNE_DAY_ONE))
        jsonObject.addProperty("isGroup", if (msgType == 1) 0 else 1)
        jsonObject.addProperty("isIntegral", getConfigVal(PluginCode.FORTUNE, PluginProperty.FORTUNE_POINT))
        jsonObject.addProperty("groupNum", groupNum.toString())
        val response = HttpUtil.post(getApiURL(PluginCode.FORTUNE), jsonObject)
            ?: return OutMessage.builder().convertFlag(false).content("运势服务故障,请联系管理员!").build()
        val type = object : TypeToken<RobotBaseResponse<FortuneResponse?>?>() {}.type
        val robotBaseResponse = JsonUtil.toObjByType<RobotBaseResponse<FortuneResponse>>(response, type)
        if (robotBaseResponse.status == 500) { //server has  error
            return null
        }
        val responseData = robotBaseResponse.data
        val dataStatus = responseData.status
        if (dataStatus == 201) {
            return null // already get fortune
        }
        if (dataStatus == 200) {
            val dataResponse = responseData.dataResponse
            return OutMessage.builder().convertFlag(true).templateCode(PluginCode.FORTUNE)
                .pluginCode(PluginCode.FORTUNE)
                .fillFlag(AppConstants.FILL_OUT_MESSAGE_OBJECT_FLAG)
                .fillObj(dataResponse).build()
        }
        return null
    }

    override fun handleGroupMessageEvent(event: GroupMessageEvent): OutMessage {
        val qq = event.sender.id
        val groupNum = event.group.id
        return getFortuneMsg(qq, groupNum, 2)!!
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent): OutMessage {
        val qq = event.sender.id
        return getFortuneMsg(qq, 0, 1)!!
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String): Boolean {
        return containCommand(PluginCode.FORTUNE, msg)
    }
}