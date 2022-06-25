package cn.zjiali.robot.handler

import cn.zjiali.robot.constant.PluginCode
import cn.zjiali.robot.factory.MessageFactory
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.service.MoLiService
import com.google.inject.Inject
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent

/**
 *
 * 一些简单的消息处理器
 * @author zJiaLi
 * @since 2021-09-04 15:40
 */
class JokeMessageEventHandler : AbstractMessageEventHandler() {

    @Inject
    private val moLiService: MoLiService? = null

    override fun handleGroupMessageEvent(event: GroupMessageEvent?): OutMessage {
        val jokeMessage = moLiService?.getJokeMessage(event!!.sender.id, true, event.group.id)
        return OutMessage.builder().pluginCode(PluginCode.JOKE).convertFlag(false).event(event).content(jokeMessage)
            .build()
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent?): OutMessage {
        val jokeMessage = moLiService?.getJokeMessage(event!!.sender.id, true, event.sender.id)
        return OutMessage.builder().pluginCode(PluginCode.JOKE).convertFlag(false).event(event).content(jokeMessage)
            .build()
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String?): Boolean {
        return containCommand(PluginCode.JOKE, msg)
    }

    override fun code(): String {
        return PluginCode.JOKE
    }
}

/**
 * 一言处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 20:57
 */
class SenMessageEventHandler : AbstractMessageEventHandler() {

    override fun handleGroupMessageEvent(event: GroupMessageEvent): OutMessage {
        return OutMessage.builder().pluginCode(PluginCode.ONE_SEN).convertFlag(false).content(MessageFactory.getSen())
            .event(event)
            .build()
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent): OutMessage {
        return OutMessage.builder().pluginCode(PluginCode.ONE_SEN).convertFlag(false)
            .event(event).content(MessageFactory.getSen())
            .build()
    }

    override fun next(): Boolean {
        return true
    }

    override fun matchCommand(msg: String): Boolean {
        return containCommand(PluginCode.ONE_SEN, msg)
    }

    override fun code(): String {
        return PluginCode.ONE_SEN
    }

}
