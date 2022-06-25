package cn.zjiali.robot.service

import cn.hutool.core.util.StrUtil
import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.MsgType
import cn.zjiali.robot.manager.RobotManager
import cn.zjiali.robot.manager.ServerConfigManager
import cn.zjiali.robot.manager.WsSecurityManager
import cn.zjiali.robot.model.response.ws.GroupAction
import cn.zjiali.robot.model.response.ws.SenderMessageRes
import cn.zjiali.robot.model.response.ws.WsClientRes
import cn.zjiali.robot.model.response.ws.WsResult
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.util.JsonUtil
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named

/**
 * @author zJiaLi
 * @since 2021-07-30 15:15
 */
@Singleton
class WebSocketService {
    private val commonLogger = CommonLogger(WebSocketService::class.java.simpleName, WebSocketService::class.java)

    @Inject
    private val robotManager: RobotManager? = null

    @Inject
    private val groupActionService: GroupActionService? = null

    @Inject
    @Named("DefaultWsSecurityManager")
    private val wsSecurityManager: WsSecurityManager? = null

    @Inject
    private val serverConfigManager: ServerConfigManager? = null

    suspend fun handleWsResult(wsResult: WsResult): String {
        val robotQQ = wsResult.robotQQ
        if (StrUtil.isNotBlank(robotQQ) && AppConfig.getQQ() == robotQQ) {
            val dataJson = wsResult.dataJson
            when (wsResult.msgType) {
                MsgType.SEND_MSG -> {
                    val senderMessageRes = JsonUtil.json2obj(dataJson, SenderMessageRes::class.java)
                    commonLogger.info(
                        "[WebSocket]====接收QQ:{} ,接收内容:{} ",
                        senderMessageRes.receiver,
                        senderMessageRes.sendMessage
                    )
                    if (StrUtil.isBlank(senderMessageRes.sendMessage)) return wsSecurityManager?.encryptMsgData(
                        WsClientRes(
                            404,
                            "消息体为空!"
                        ).toJson()
                    )!!
                    when (senderMessageRes.sendFlag) {
                        MsgType.SEND_FRIEND_MSG -> {
                            senderMessageRes.receiverList!!.forEach {
                                robotManager!!.sendFriendMessage(
                                    it.toLong(),
                                    senderMessageRes.sendMessage
                                )
                            }
                        }

                        MsgType.SEND_GROUP_AT_MSG -> robotManager!!.sendGroupAtMessage(
                            senderMessageRes.receiver!!.toLong(),
                            senderMessageRes.sendGroup!!.toLong(),
                            senderMessageRes.sendMessage
                        )

                        MsgType.SEND_GROUP_MSG -> {
                            senderMessageRes.sendGroupList!!.forEach {
                                robotManager!!.sendGroupMessage(
                                    it.toLong(),
                                    senderMessageRes.sendMessage
                                )
                            }
                        }
                    }
                }
                MsgType.GROUP_ACTION -> {
                    val groupAction = JsonUtil.json2obj(dataJson, GroupAction::class.java)
                    groupActionService!!.doAction(groupAction)
                }
                MsgType.CONFIG_ACTION -> {
                    serverConfigManager?.pullServerConfig()
                }
            }
        }
        return wsSecurityManager?.encryptMsgData(WsClientRes(200, "处理成功!").toJson())!!
    }
}