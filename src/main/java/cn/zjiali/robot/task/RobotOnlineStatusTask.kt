package cn.zjiali.robot.task

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.MsgType
import cn.zjiali.robot.main.websocket.WebSocketManager
import cn.zjiali.robot.manager.RobotManager
import cn.zjiali.robot.model.response.ws.WsResult
import cn.zjiali.robot.util.GuiceUtil
import cn.zjiali.robot.util.JsonUtil
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Runnable
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.exitProcess

/**
 * @author zJiaLi
 * @since 2023-06-29 15:12
 */
class RobotOnlineStatusTask : Runnable {
    private var wsResult: WsResult = WsResult()
    private val reConnectCount: AtomicInteger = AtomicInteger(1)
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    init {
        wsResult.msgType = MsgType.ONLINE_STATUS
        wsResult.robotQQ = AppConfig.getQQ()
        wsResult.timestamp = System.currentTimeMillis()
    }

    override fun run() {
        val robotManager = GuiceUtil.getBean(RobotManager::class.java)
        val online = robotManager.botInstance()!!.isOnline
        wsResult.dataJson = """
            {"online":$online}
        """.trimIndent()
        val webSocketManager = GuiceUtil.getBean(WebSocketManager::class.java)
        webSocketManager.sendText(JsonUtil.obj2str(wsResult))
        if (!online) {
            val count = reConnectCount.getAndIncrement()
            if (count <= 3) {
                logger.info("...机器人尝试第{}次重新连接...", count)
                runBlocking { robotManager.init() }
            } else {
                exitProcess(0)
            }
        }
    }
}
