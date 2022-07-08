package cn.zjiali.robot.handler

import cn.zjiali.robot.constant.AppConstants
import cn.zjiali.robot.constant.PluginCode
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.model.response.RequestSongResponse
import cn.zjiali.robot.model.response.RobotBaseResponse
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*

/**
 * 万年历处理器
 *
 * @author zJiaLi
 * @since 2021-04-04 11:02
 */
class RequestSongMessageEventHandler : AbstractMessageEventHandler() {
    override fun handleGroupMessageEvent(event: GroupMessageEvent): OutMessage {
        val songName = event.message.content.replace(getCommand(PluginCode.REQUEST_SONG, event), "").trim()
        return requestSong(songName, event)!!
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent): OutMessage {
        val songName = event.message.content.replace(getCommand(PluginCode.REQUEST_SONG, event), "").trim()
        return requestSong(songName, event)!!
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String): Boolean {
        return msg.startsWith(getCommand(PluginCode.REQUEST_SONG))
    }

    override fun matchCommand(messageEvent: MessageEvent?): Boolean {
        val msg = messageEvent!!.message.contentToString()
        return msg.startsWith(getCommand(PluginCode.REQUEST_SONG, messageEvent))
    }

    override fun code(): String {
        return PluginCode.REQUEST_SONG
    }


    private fun requestSong(songName: String, event: MessageEvent): OutMessage? {
        val postData = JsonObject()
        postData.addProperty("keyword", songName)
        postData.addProperty("type", "qq")
        val response = HttpUtil.post(getApiURL(PluginCode.REQUEST_SONG, event), postData)
        val type = object : TypeToken<RobotBaseResponse<RequestSongResponse?>?>() {}.type
        val baseResponse = JsonUtil.toObjByType<RobotBaseResponse<RequestSongResponse>>(response, type)
        if (baseResponse.success()) {
            val songResponse = baseResponse.data
            val musicInfo = songResponse.musicInfo
            val musicShare = MusicShare(
                MusicKind.QQMusic,
                musicInfo.title,
                musicInfo.desc,
                musicInfo.jurl,
                musicInfo.purl,
                musicInfo.murl
            )
            return OutMessage.builder().convertFlag(false)
                .messageType(AppConstants.MESSAGE_TYPE_PLUGIN)
                .message(musicShare)
                .pluginCode(PluginCode.REQUEST_SONG)
                .build()
        }
        return null
    }

}