package cn.zjiali.robot.main.websocket

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.manager.WsSecurityManager
import cn.zjiali.robot.model.response.ws.WsClientRes
import cn.zjiali.robot.util.PropertiesUtil
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import kotlinx.coroutines.*
import java.io.IOException

/**
 * @author zJiaLi
 * @since 2021-07-29 10:18
 */
@Singleton
class WebSocketManager {

    private val webSocketFactory: WebSocketFactory? = null

    @Inject
    @Named("DefaultWsSecurityManager")
    private val wsSecurityManager: WsSecurityManager? = null

    @Throws(IOException::class)
    fun connect() {
        val webSocketUrl = PropertiesUtil.getApplicationProperty("robot.websocket.url")
        webSocketFactory!!.connect(
            webSocketUrl,
            AppConfig.getQQ(),
            wsSecurityManager!!.genWsToken(AppConfig.getQQ())
        )
        sendText(
            WsClientRes(
                200,
                "连接成功!"
            ).toJson()
        )
    }

    fun sendText(text: String) {
        this.webSocketFactory!!.channel().writeAndFlush(TextWebSocketFrame(wsSecurityManager!!.encryptMsgData(text)))
    }


    fun close() {
        this.webSocketFactory!!.close()
    }
}