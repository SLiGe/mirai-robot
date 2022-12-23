package cn.zjiali.robot.manager

import cn.zjiali.robot.util.HttpUtil
import com.google.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.inputStream

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
        val tempBqbPath = Path.of(System.getProperty("robot.workdir"), "tempDir", "bqb")
        Files.exists(tempBqbPath).let {
            if (!it) {
                Files.createDirectories(tempBqbPath)
            }
        }
    }


    suspend fun sendFriendMessage(qq: Long, message: String?) {
        val friend = bot!!.getFriend(qq)
        friend?.sendMessage(message!!)
    }

    suspend fun sendFriendImage(qq: Long, imageName: String, imgUrl: String?) {
        val tempFile = Path.of(System.getProperty("robot.workdir"), "tempDir", "bqb", imageName)
        try {
            val friend = bot!!.getFriend(qq)
            val imageStream = imageStream(tempFile, imgUrl)
            friend?.sendImage(imageStream)
        } finally {
            withContext(Dispatchers.IO) {
                Files.delete(tempFile)
            }
        }

    }

    suspend fun sendGroupMessage(groupId: Long, message: String?) {
        sendGroup(groupId, PlainText(message!!))
    }

    suspend fun sendGroupImage(groupId: Long, imageName: String, imgUrl: String?) {
        val tempFile = Path.of(System.getProperty("robot.workdir"), "tempDir", "bqb", imageName)
        val group = bot!!.getGroup(groupId)
        val imageStream = imageStream(tempFile, imgUrl)
        try {
            group?.sendImage(imageStream)
        } finally {
            withContext(Dispatchers.IO) {
                Files.delete(tempFile)
            }
        }
    }

    private fun imageStream(imagePath: Path, imgUrl: String?): InputStream {
        val fileBytes = HttpUtil.fileBytes(imgUrl)
        return Files.write(imagePath, fileBytes, StandardOpenOption.CREATE_NEW).inputStream()
    }

    suspend fun sendGroupAtMessage(qq: Long, groupId: Long, message: String?) {
        sendGroup(groupId, At(qq).plus(message!!))
    }

    private suspend fun sendGroup(groupId: Long, message: Message?) {
        val group = bot!!.getGroup(groupId)
        group?.sendMessage(message!!)
    }
}