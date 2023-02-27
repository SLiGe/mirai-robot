package cn.zjiali.robot.manager

import cn.hutool.core.exceptions.ExceptionUtil
import cn.hutool.core.lang.UUID
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.util.HttpUtil
import com.google.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

/**
 * @author zJiaLi
 * @since 2021-07-30 16:02
 */
@Singleton
class RobotManager {
    var bot: Bot? = null
        private set
    private val logger = CommonLogger(RobotManager::class.java)

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
        val finalImageName = UUID.randomUUID().toString() + imageName.substring(imageName.lastIndexOf("."))
        val tempFile = Path.of(System.getProperty("robot.workdir"), "tempDir", "bqb", finalImageName)
        val imageStream = imageStream(tempFile, imgUrl)
        val friend = bot!!.getFriend(qq)
        try {
            friend?.sendImage(imageStream)
        } catch (e: Exception) {
            logger.warning("发送好友图片失败: {}", ExceptionUtil.stacktraceToString(e))
        } finally {
            withContext(Dispatchers.IO) {
                imageStream.close()
                Files.delete(tempFile)
            }
        }

    }

    suspend fun sendGroupMessage(groupId: Long, message: String?) {
        sendGroup(groupId, PlainText(message!!))
    }

    suspend fun sendGroupImage(groupId: Long, imageName: String, imgUrl: String?) {
        val finalImageName = UUID.randomUUID().toString() + imageName.substring(imageName.lastIndexOf("."))
        val tempFile = Path.of(System.getProperty("robot.workdir"), "tempDir", "bqb", finalImageName)
        val imageStream = imageStream(tempFile, imgUrl)
        val group = bot!!.getGroup(groupId)
        try {
//            group?.sendImage(imageStream)
            val uploadImage = group?.uploadImage(imageStream)
            group?.sendMessage(Image(uploadImage!!.imageId))
            logger.info("发送群图片成功,文件路径:{}", tempFile)
        } catch (e: Exception) {
            logger.warning("发送群图片失败: {}", ExceptionUtil.stacktraceToString(e))
        } finally {
            withContext(Dispatchers.IO) {
                imageStream.close()
                Files.delete(tempFile)
            }
        }
    }

    private fun imageStream(imagePath: Path, imgUrl: String?): InputStream {
        val fileBytes = HttpUtil.fileBytes(imgUrl)
        Files.write(imagePath, fileBytes, StandardOpenOption.CREATE_NEW)
        return Files.newInputStream(imagePath, StandardOpenOption.READ)
    }

    suspend fun sendGroupAtMessage(qq: Long, groupId: Long, message: String?) {
        sendGroup(groupId, At(qq).plus(message!!))
    }

    private suspend fun sendGroup(groupId: Long, message: Message?) {
        val group = bot!!.getGroup(groupId)
        group?.sendMessage(message!!)
    }
}