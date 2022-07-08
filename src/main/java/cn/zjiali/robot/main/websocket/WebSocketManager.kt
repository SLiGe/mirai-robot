package cn.zjiali.robot.main.websocket

import java.util.concurrent.ConcurrentHashMap
import cn.zjiali.robot.service.WebSocketService
import cn.zjiali.robot.util.CommonLogger
import kotlin.Throws
import java.io.IOException
import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.manager.WsSecurityManager
import cn.zjiali.robot.model.response.ws.WsClientRes
import cn.zjiali.robot.model.response.ws.WsResult
import cn.zjiali.robot.util.JsonUtil
import cn.zjiali.robot.util.PropertiesUtil
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

/**
 * @author zJiaLi
 * @since 2021-07-29 10:18
 */
@Singleton
class WebSocketManager {
    private val webSocketClientMap: MutableMap<String, WebSocketClient> = ConcurrentHashMap()

    @Inject
    private val webSocketService: WebSocketService? = null

    @Inject
    @Named("DefaultWsSecurityManager")
    private val wsSecurityManager: WsSecurityManager? = null
    private val commonLogger = CommonLogger(WebSocketManager::class.java)

    @Throws(IOException::class)
    fun connect() {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .pingInterval(20, TimeUnit.SECONDS)
            .build()
        val webSocketUrl = PropertiesUtil.getApplicationProperty("robot.websocket.url")
        val request: Request = Request.Builder().url(webSocketUrl)
            .header("robotQQ", AppConfig.getQQ())
            .header("ws-token", wsSecurityManager!!.genWsToken(AppConfig.getQQ()))
            .build()
        val webSocket = okHttpClient.newWebSocket(request, RobotWebSocketListener())
        webSocket.send(
            wsSecurityManager.encryptMsgData(WsClientRes(200, "连接成功!").toJson())
        )
        putSession(webSocket)
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun handleMessage(webSocket: WebSocket, text: String?) {
        val decryptMsgData = wsSecurityManager!!.decryptMsgData(text)
        commonLogger.info("[WebSocket]====收到消息: {}", decryptMsgData)
        try {
            val wsResult = JsonUtil.json2obj(decryptMsgData, WsResult::class.java)
            GlobalScope.launch(Dispatchers.Unconfined) {
                val handleWsResult = webSocketService!!.handleWsResult(wsResult)
                webSocket.send(handleWsResult)
            }
        } catch (_: Exception) {
        }

    }

    fun getSession(robotQQ: String): WebSocket? {
        return if (webSocketClientMap[robotQQ] != null) webSocketClientMap[robotQQ]?.webSocket else null
    }

    fun putSession(webSocket: WebSocket?) {
        webSocketClientMap[AppConfig.getQQ()] =
            WebSocketClient(AppConfig.getQQ(), webSocket)
    }

    fun removeSession() {
        webSocketClientMap.remove(AppConfig.getQQ())
    }

    fun removeSession(robotQQ: String) {
        webSocketClientMap.remove(robotQQ)
    }
}