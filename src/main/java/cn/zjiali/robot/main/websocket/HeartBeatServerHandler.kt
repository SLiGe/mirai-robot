package cn.zjiali.robot.main.websocket

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.MsgType
import cn.zjiali.robot.manager.WsSecurityManager
import cn.zjiali.robot.model.response.ws.WsClientRes
import cn.zjiali.robot.model.response.ws.WsResult
import cn.zjiali.robot.util.GuiceUtil
import cn.zjiali.robot.util.JsonUtil
import io.grpc.netty.shaded.io.netty.channel.ChannelDuplexHandler
import io.grpc.netty.shaded.io.netty.channel.ChannelHandlerContext
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.grpc.netty.shaded.io.netty.handler.timeout.IdleState
import io.grpc.netty.shaded.io.netty.handler.timeout.IdleStateEvent

/**
 *
 * @author zJiaLi
 * @since 2023-02-21 11:02
 */
class HeartBeatServerHandler : ChannelDuplexHandler() {

    private var wsResult: WsResult = WsResult()

    init {
        wsResult.msgType = MsgType.CONNECT
        wsResult.robotQQ = AppConfig.getQQ()
        wsResult.timestamp = System.currentTimeMillis()
        wsResult.dataJson = WsClientRes(
            200,
            "client online!"
        ).toJson()
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        if (evt is IdleStateEvent) {
            val state = evt.state()
            if (state.equals(IdleState.WRITER_IDLE) || state.equals(IdleState.READER_IDLE)) {
                val wsSecurityManager = GuiceUtil.getBean("DefaultWsSecurityManager", WsSecurityManager::class.java)
                ctx!!.channel()!!.writeAndFlush(
                    TextWebSocketFrame(
                        wsSecurityManager!!.encryptMsgData(
                            JsonUtil.obj2str(wsResult)
                        )
                    )
                )
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}