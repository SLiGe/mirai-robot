package cn.zjiali.robot.task

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.factory.ServiceFactory
import cn.zjiali.robot.main.websocket.WebSocketManager
import cn.zjiali.robot.manager.WsSecurityManager
import cn.zjiali.robot.model.response.ws.WsClientRes

/**
 * @author zJiaLi
 * @since 2022-02-18 17:22
 */
class WebSocketStatusTask : Runnable {

    override fun run() {
        val webSocketManager = ServiceFactory.getInstance().getBean(
            WebSocketManager::class.java.simpleName,
            WebSocketManager::class.java
        )
        val wsSecurityManager = ServiceFactory.getInstance().get(
            "DefaultWsSecurityManager",
            WsSecurityManager::class.java
        )
        val session = webSocketManager.getSession(AppConfig.getQQ())
        val sendFlag = session!!.send(wsSecurityManager.encryptMsgData(WsClientRes(200, "存活检测!").toJson()))
        if (!sendFlag) {
            session.close(1000, "manual close")
            webSocketManager.removeSession()
            webSocketManager.connect()
        }

    }
}