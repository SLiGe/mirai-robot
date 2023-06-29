package cn.zjiali.robot.task

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.MsgType
import cn.zjiali.robot.main.websocket.WebSocketManager
import cn.zjiali.robot.manager.RobotManager
import cn.zjiali.robot.model.response.ws.WsResult
import cn.zjiali.robot.util.GuiceUtil
import cn.zjiali.robot.util.JsonUtil

/**
 * @author zJiaLi
 * @since 2023-06-29 15:12
 */
class RobotOnlineStatusTask : Runnable {
    private var wsResult: WsResult = WsResult()

    init {
        wsResult.msgType = MsgType.ONLINE_STATUS
        wsResult.robotQQ = AppConfig.getQQ()
        wsResult.timestamp = System.currentTimeMillis()
    }

    override fun run() {
        val robotManager = GuiceUtil.getBean(RobotManager::class.java)
        val online = robotManager.bot!!.isOnline
        wsResult.dataJson = """
            {"online":$online}
        """.trimIndent()
        val webSocketManager = GuiceUtil.getBean(WebSocketManager::class.java)
        webSocketManager.sendText(JsonUtil.obj2str(wsResult))
    }
}
