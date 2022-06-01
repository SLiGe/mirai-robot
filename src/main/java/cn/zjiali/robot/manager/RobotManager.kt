package cn.zjiali.robot.manager

import com.google.inject.Singleton
import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText

/**
 * @author zJiaLi
 * @since 2021-07-30 16:02
 */
@Singleton
class RobotManager {
    var bot: Bot? = null
        private set

    fun init(bot: Bot?) {
        this.bot = bot
    }

    suspend fun sendFriendMessage(qq: Long, message: String?) {
        val friend = bot!!.getFriend(qq)
        friend?.sendMessage(message!!)
    }

    suspend fun sendGroupMessage(groupId: Long, message: String?) {
        sendGroup(groupId, PlainText(message!!))
    }

    suspend fun sendGroupAtMessage(qq: Long, groupId: Long, message: String?) {
        sendGroup(groupId, At(qq).plus(message!!))
    }

    suspend fun sendGroup(groupId: Long, message: Message?) {
        val group = bot!!.getGroup(groupId)
        group?.sendMessage(message!!)
    }
}