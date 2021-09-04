package cn.zjiali.robot.handler

import cn.zjiali.robot.annotation.Autowired
import cn.zjiali.robot.constant.PluginCode
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.service.MoLiService
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent

/**
 *
 *
 * @author zJiaLi
 * @since 2021-09-04 15:40
 */
class JokeMessageEventHandler : AbstractMessageEventHandler() {

    @Autowired
    private val moLiService: MoLiService? = null

    override fun handleGroupMessageEvent(event: GroupMessageEvent?): OutMessage {
        val jokeMessage = moLiService?.getJokeMessage(event!!.sender.id, true, event.group.id)
        return OutMessage.builder().convertFlag(false).content(jokeMessage).build()
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent?): OutMessage {
        val jokeMessage = moLiService?.getJokeMessage(event!!.sender.id, true, event.sender.id)
        return OutMessage.builder().convertFlag(false).content(jokeMessage).build()
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String?): Boolean {
        return containCommand(PluginCode.JOKE, msg)
    }
}

class YlLqMessageEventHandler : AbstractMessageEventHandler() {
    @Autowired
    private val moLiService: MoLiService? = null

    override fun handleGroupMessageEvent(event: GroupMessageEvent?): OutMessage {
        val ylMessage = moLiService?.getYllqMessage(event!!.sender.id, true, event.group.id)
        return OutMessage.builder().convertFlag(false).content(ylMessage).build()
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent?): OutMessage {
        val ylMessage = moLiService?.getYllqMessage(event!!.sender.id, true, event.sender.id)
        return OutMessage.builder().convertFlag(false).content(ylMessage).build()
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String?): Boolean {
        return containCommand(PluginCode.YL_LQ, msg)
    }
}

class GyLqMessageEventHandler : AbstractMessageEventHandler() {
    @Autowired
    private val moLiService: MoLiService? = null

    override fun handleGroupMessageEvent(event: GroupMessageEvent?): OutMessage {
        val gyMessage = moLiService?.getGylqMessage(event!!.sender.id, true, event.group.id)
        return OutMessage.builder().convertFlag(false).content(gyMessage).build()
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent?): OutMessage {
        val gyMessage = moLiService?.getGylqMessage(event!!.sender.id, true, event.sender.id)
        return OutMessage.builder().convertFlag(false).content(gyMessage).build()
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String?): Boolean {
        return containCommand(PluginCode.GY_LQ, msg)
    }

}


class CsyLqMessageEventHandler : AbstractMessageEventHandler() {
    @Autowired
    private val moLiService: MoLiService? = null

    override fun handleGroupMessageEvent(event: GroupMessageEvent?): OutMessage {
        val csyMessage = moLiService?.getCsylqMessage(event!!.sender.id, true, event.group.id)
        return OutMessage.builder().convertFlag(false).content(csyMessage).build()
    }

    override fun handleFriendMessageEvent(event: FriendMessageEvent?): OutMessage {
        val csyMessage = moLiService?.getCsylqMessage(event!!.sender.id, true, event.sender.id)
        return OutMessage.builder().convertFlag(false).content(csyMessage).build()
    }

    override fun next(): Boolean {
        return false
    }

    override fun matchCommand(msg: String?): Boolean {
        return containCommand(PluginCode.CSY_LQ, msg)
    }
}
