package cn.zjiali.robot.main.websocket

import cn.zjiali.robot.manager.WsSecurityManager
import cn.zjiali.robot.model.response.ws.WsResult
import cn.zjiali.robot.service.WebSocketService
import cn.zjiali.robot.util.GuiceUtil
import cn.zjiali.robot.util.JsonUtil
import io.grpc.netty.shaded.io.netty.channel.ChannelHandlerContext
import io.grpc.netty.shaded.io.netty.channel.ChannelPromise
import io.grpc.netty.shaded.io.netty.channel.SimpleChannelInboundHandler
import io.grpc.netty.shaded.io.netty.handler.codec.http.FullHttpResponse
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.*
import io.grpc.netty.shaded.io.netty.util.CharsetUtil
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

/**
 *
 * @author zJiaLi
 * @since 2023-02-21 11:22
 */
class WebSocketClientHandler(
    private val handshaker: WebSocketClientHandshaker,
    private val wsSecurityManager: WsSecurityManager,
    private val webSocketService: WebSocketService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SimpleChannelInboundHandler<Any>() {

    lateinit var handshakeFuture: ChannelPromise

    private val logger = LoggerFactory.getLogger(WebSocketClientHandler::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    override fun channelRead0(p0: ChannelHandlerContext?, msg: Any?) {
        val channel = p0!!.channel()
        if (!handshaker.isHandshakeComplete) {
            try {
                handshaker.finishHandshake(channel, msg as FullHttpResponse?)
                logger.info("WebSocket Client connected!")
                handshakeFuture.setSuccess()
            } catch (e: WebSocketHandshakeException) {
                logger.info("WebSocket Client failed to connect")
                handshakeFuture.setFailure(e)
            }
            return
        }

        if (msg is FullHttpResponse) {
            val response: FullHttpResponse =
                msg
            throw IllegalStateException(
                "Unexpected FullHttpResponse (getStatus=" + response.status() +
                        ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')'
            )
        }

        val frame: WebSocketFrame =
            msg as WebSocketFrame
        when (frame) {
            is TextWebSocketFrame -> {
                val textFrame: TextWebSocketFrame =
                    frame
                val decryptMsgData = wsSecurityManager.decryptMsgData(textFrame.text())
                logger.info("[WebSocket]====收到消息: {}", decryptMsgData)
                val wsResult = JsonUtil.json2obj(decryptMsgData, WsResult::class.java)
                GlobalScope.launch(dispatcher) {
                    val handleWsResult = webSocketService.handleWsResult(wsResult)
                    channel.writeAndFlush(TextWebSocketFrame(handleWsResult))
                }
            }

            is PongWebSocketFrame -> {
                val pongFrame: PongWebSocketFrame = frame
                logger.debug(
                    "WebSocket Client received pong {}",
                    pongFrame.content().toString(StandardCharsets.UTF_8)
                )
            }

            is CloseWebSocketFrame -> {
                logger.info("WebSocket Client received closing")
                p0.close()
            }
        }

    }

    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        this.handshakeFuture = ctx!!.newPromise()
    }

    override fun channelActive(ctx: ChannelHandlerContext?) {
        this.handshaker.handshake(ctx!!.channel())
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        logger.info("WebSocket Client disconnected!")
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext?) {
        //reconnect
        logger.info("WebSocket after 5's reconnect!")
        ctx!!.channel().eventLoop().schedule({
            logger.info("WebSocket reconnect!")
            GuiceUtil.getBean(WebSocketManager::class.java).connect()
        }, 5, TimeUnit.SECONDS)
    }

    @Deprecated("Deprecated in Java")
    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        cause!!.printStackTrace()
        if (!handshakeFuture.isDone) {
            handshakeFuture.setFailure(cause)
        }
        ctx!!.close()
    }
}