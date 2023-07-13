package cn.zjiali.robot.manager

import cn.hutool.core.exceptions.ExceptionUtil
import cn.hutool.core.lang.UUID
import cn.zjiali.robot.RobotApplication
import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.handler.GlobalEventHandler
import cn.zjiali.robot.handler.GlobalMessageHandler
import cn.zjiali.robot.main.system.SysLoginSolver
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.util.GuiceUtil
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.ObjectUtil
import com.google.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory.INSTANCE.newBot
import net.mamoe.mirai.auth.BotAuthorization
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol
import xyz.cssxsh.mirai.tool.FixProtocolVersion
import xyz.cssxsh.mirai.tool.FixProtocolVersion.info
import xyz.cssxsh.mirai.tool.FixProtocolVersion.update
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*

private const val ROBOT_WORKDIR = "robot.workdir"

/**
 * @author zJiaLi
 * @since 2021-07-30 16:02
 */
@Singleton
class RobotManager(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    private var botInstance: Bot? = null
    private val logger = CommonLogger(RobotManager::class.java)

    fun init(bot: Bot?) {
        this.botInstance = bot
        val tempBqbPath = Path.of(System.getProperty(ROBOT_WORKDIR), "tempDir", "bqb")
        Files.exists(tempBqbPath).let {
            if (!it) {
                Files.createDirectories(tempBqbPath)
            }
        }
    }

    fun initBotBlocking(): Bot = runBlocking { init() }
    fun botInstance(): Bot? {
        return this.botInstance
    }

    suspend fun init(): Bot {
        this.botInstance?.close()
        val qq = Objects.requireNonNull(AppConfig.applicationConfig.qq)!!.toLong()
        val password = AppConfig.applicationConfig.password!!
        update()
        val botConfiguration = BotConfiguration()
        //设置登录解决器
        //设置登录解决器
        botConfiguration.loginSolver = GuiceUtil.getBean(SysLoginSolver::class.java)
        // 选择协议
        // 选择协议
        botConfiguration.protocol = switchProtocol()
        botConfiguration.cacheDir = File(System.getProperty(ROBOT_WORKDIR) + "/cache")
        botConfiguration.workingDir = File(System.getProperty(ROBOT_WORKDIR))
        botConfiguration.fileBasedDeviceInfo("device.json")
        val loginType = System.getProperty("login.type")
        val bot: Bot = if ("qr" == loginType) {
            //扫码登录只支持2-watch和4-mac协议
            newBot(qq, BotAuthorization.byQRCode(), botConfiguration)
        } else {
            newBot(qq, password, botConfiguration)
        }
        bot.login()
        RobotApplication.initLatch.countDown()
        val eventChannel = bot.eventChannel
        // 创建监听
        eventChannel.exceptionHandler { e: Throwable? ->
            logger.error("unknown error: {}", ExceptionUtil.stacktraceToString(e))
        }
        //保存robot实例
        init(bot)
        val globalMessageHandler = GuiceUtil.getBean(GlobalMessageHandler::class.java)
        eventChannel.subscribeAlways<GroupMessageEvent> {
            globalMessageHandler.handleGroupMessageEvent(
                it
            )
        }
        eventChannel.subscribeAlways<FriendMessageEvent> {
            globalMessageHandler.handleFriendMessageEvent(
                it
            )
        }
        eventChannel.subscribeAlways<StrangerMessageEvent> {
            globalMessageHandler.handleOtherMessageEvent(
                it
            )
        }
        eventChannel.subscribeAlways<NewFriendRequestEvent> {
            it.accept()
        }

        eventChannel.subscribeAlways<GroupTempMessageEvent> { globalMessageHandler.handleOtherMessageEvent(it) }
        val globalEventHandler = GuiceUtil.getBean(GlobalEventHandler::class.java)
        eventChannel.subscribeAlways<BotInvitedJoinGroupRequestEvent> { event: BotInvitedJoinGroupRequestEvent? ->
            globalEventHandler.handleBotInvitedJoinGroupRequestEvent(
                event
            )
        }
        logger.info("Fix protocol Version: ", info())
        return bot
    }


    suspend fun sendFriendMessage(qq: Long, message: String?) {
        val friend = botInstance!!.getFriend(qq)
        friend?.sendMessage(message!!)
    }

    suspend fun sendFriendImage(qq: Long, imageName: String, imgUrl: String?) {
        val finalImageName = UUID.randomUUID().toString() + imageName.substring(imageName.lastIndexOf("."))
        val tempFile = Path.of(System.getProperty(ROBOT_WORKDIR), "tempDir", "bqb", finalImageName)
        val imageStream = imageStream(tempFile, imgUrl)
        val friend = botInstance!!.getFriend(qq)
        try {
            friend?.sendImage(imageStream)
        } catch (e: Exception) {
            logger.warning("发送好友图片失败: {}", ExceptionUtil.stacktraceToString(e))
        } finally {
            withContext(ioDispatcher) {
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
        val tempFile = Path.of(System.getProperty(ROBOT_WORKDIR), "tempDir", "bqb", finalImageName)
        val imageStream = imageStream(tempFile, imgUrl)
        val group = botInstance!!.getGroup(groupId)
        try {
            val uploadImage = group?.uploadImage(imageStream)
            group?.sendMessage(Image(uploadImage!!.imageId))
            logger.info("发送群图片成功,文件路径:{}", tempFile)
        } catch (e: Exception) {
            logger.warning("发送群图片失败: {}", ExceptionUtil.stacktraceToString(e))
        } finally {
            withContext(ioDispatcher) {
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
        val group = botInstance!!.getGroup(groupId)
        group?.sendMessage(message!!)
    }

    /**
     * 选择协议
     *
     * @return 协议
     */
    private fun switchProtocol(): MiraiProtocol {
        val robotProtocol = System.getProperty("robot.protocol")
        val protocol: MiraiProtocol = if (!ObjectUtil.isNullOrEmpty(robotProtocol)) {
            when (robotProtocol) {
                "1" -> {
                    MiraiProtocol.ANDROID_PAD
                }

                "2" -> {
                    MiraiProtocol.ANDROID_WATCH
                }

                "3" -> {
                    MiraiProtocol.IPAD
                }

                "4" -> {
                    MiraiProtocol.MACOS
                }

                else -> {
                    MiraiProtocol.ANDROID_PHONE
                }
            }
        } else {
            MiraiProtocol.ANDROID_PHONE
        }
        if (protocol !== MiraiProtocol.ANDROID_WATCH) {
            FixProtocolVersion.sync(protocol)
            FixProtocolVersion.load(protocol)
        }
        return protocol
    }
}