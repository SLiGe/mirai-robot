package cn.zjiali.robot.main.websocket

import cn.hutool.core.exceptions.ExceptionUtil
import cn.zjiali.robot.manager.WsSecurityManager
import cn.zjiali.robot.service.WebSocketService
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.grpc.netty.shaded.io.netty.bootstrap.Bootstrap
import io.grpc.netty.shaded.io.netty.channel.Channel
import io.grpc.netty.shaded.io.netty.channel.ChannelOption
import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup
import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioSocketChannel
import io.grpc.netty.shaded.io.netty.handler.codec.http.DefaultHttpHeaders
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.WebSocketVersion
import org.slf4j.LoggerFactory
import java.net.URI

/**
 *
 * @author zJiaLi
 * @since 2023-02-21 10:34
 */
@Singleton
class WebSocketFactory {

    private var channel: Channel? = null

    private val eventLoopGroup = NioEventLoopGroup()

    private val logger = LoggerFactory.getLogger(WebSocketFactory::class.java)

    @Inject
    @Named("DefaultWsSecurityManager")
    private val wsSecurityManager: WsSecurityManager? = null

    @Inject
    private val webSocketService: WebSocketService? = null


    fun connect(uri: String, qq: String, token: String): Channel {
        val bootstrap = Bootstrap()
        val websocketAddress = URI(uri)
        val host = websocketAddress.host
        val port = if (websocketAddress.port == -1) 443 else websocketAddress.port
        val scheme = websocketAddress.scheme
        val ssl = scheme.equals("wss", true)
        val httpHeaders = DefaultHttpHeaders()
        httpHeaders["ws-token"] = token
        httpHeaders["robotQQ"] = qq
        //握手
        val handshaker = WebSocketClientHandshakerFactory.newHandshaker(
            websocketAddress,
            WebSocketVersion.V13,
            null,
            true,
            httpHeaders
        )
        val webSocketClientHandler = WebSocketClientHandler(handshaker, wsSecurityManager!!, webSocketService!!)

        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
            .group(eventLoopGroup)
            .channel(NioSocketChannel::class.java)
            .handler(WsChannelInitializer(host, port, ssl, webSocketClientHandler))
        val channel = bootstrap.connect(host, port).addListener {
            if (it.cause() != null) {
                logger.error("Websocket connect failed! e:{}", ExceptionUtil.stacktraceToString(it.cause()))
            }
        }.sync().channel()
        this.channel = channel
        webSocketClientHandler.handshakeFuture.sync()
        return channel

    }

    fun channel(): Channel {
        return this.channel!!
    }


    fun close() {
        this.eventLoopGroup.shutdownGracefully()
    }


}