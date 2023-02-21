package cn.zjiali.robot.main.websocket

import io.grpc.netty.shaded.io.netty.channel.ChannelInitializer
import io.grpc.netty.shaded.io.netty.channel.socket.SocketChannel
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpClientCodec
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpObjectAggregator
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.grpc.netty.shaded.io.netty.handler.timeout.IdleStateHandler
import java.util.concurrent.TimeUnit

class WsChannelInitializer(
    private val host: String,
    private val port: Int,
    private val webSocketClientHandler: WebSocketClientHandler
) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(p0: SocketChannel?) {
        val pipeline = p0!!.pipeline()
        val sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
        pipeline.addLast(sslContext.newHandler(p0.alloc(), host, port))
        pipeline.addLast(
            HttpClientCodec(),
            HttpObjectAggregator(1024 * 1024 * 10),
            WebSocketClientCompressionHandler.INSTANCE,
            webSocketClientHandler
        )
        pipeline.addLast(IdleStateHandler(0, 4, 0, TimeUnit.SECONDS))
        pipeline.addLast(HeartBeatServerHandler())
    }

}