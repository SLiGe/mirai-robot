package cn.zjiali.robot.main.websocket

import cn.zjiali.robot.util.GuiceUtil
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

/**
 * @author zJiaLi
 * @since 2021-07-29 10:19
 */
class RobotWebSocketListener : WebSocketListener() {
    private val webSocketManager = GuiceUtil.getBean(
        WebSocketManager::class.java
    )

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        webSocketManager.removeSession()
        super.onClosed(webSocket, code, reason)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocketManager.removeSession()
        super.onClosing(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        webSocketManager.removeSession()
        super.onFailure(webSocket, t, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        webSocketManager.handleMessage(webSocket, text)
        super.onMessage(webSocket, text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        webSocketManager.handleMessage(webSocket, bytes.toString())
        super.onMessage(webSocket, bytes)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        webSocketManager.putSession(webSocket)
        super.onOpen(webSocket, response)
    }
}