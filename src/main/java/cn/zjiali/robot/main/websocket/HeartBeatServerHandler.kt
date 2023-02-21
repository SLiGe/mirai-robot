package cn.zjiali.robot.main.websocket

import io.grpc.netty.shaded.io.netty.channel.ChannelDuplexHandler
import io.grpc.netty.shaded.io.netty.channel.ChannelHandlerContext
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.PingWebSocketFrame
import io.grpc.netty.shaded.io.netty.handler.timeout.IdleState
import io.grpc.netty.shaded.io.netty.handler.timeout.IdleStateEvent

/**
 *
 * @author zJiaLi
 * @since 2023-02-21 11:02
 */
class HeartBeatServerHandler : ChannelDuplexHandler() {
    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        if (evt is IdleStateEvent) {
            val state = evt.state()
            if (state.equals(IdleState.WRITER_IDLE)) {
                ctx!!.writeAndFlush(PingWebSocketFrame())
            }
        }
    }
}