package cn.zjiali.robot.service

import cn.hutool.core.util.StrUtil
import cn.zjiali.robot.annotation.Service
import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.MsgType
import cn.zjiali.robot.factory.DefaultBeanFactory
import cn.zjiali.robot.manager.RobotManager
import cn.zjiali.robot.manager.WsSecurityManager
import cn.zjiali.robot.model.response.ws.SenderMessageRes
import cn.zjiali.robot.model.response.ws.WsClientRes
import cn.zjiali.robot.model.response.ws.WsResult
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.util.JsonUtil

/**
 * @author zJiaLi
 * @since 2021-07-30 15:15
 */
@Service
class WebSocketService {
    private val commonLogger = CommonLogger(WebSocketService::class.java.simpleName, WebSocketService::class.java)

    suspend fun handleWsResult(wsResult: WsResult): String {
        val robotQQ = wsResult.robotQQ
        val robotManager: RobotManager? =
            DefaultBeanFactory.getInstance().getBean(RobotManager::class.java.simpleName, RobotManager::class.java)
        val wsSecurityManager: WsSecurityManager? =
            DefaultBeanFactory.getInstance().getBean("DefaultWsSecurityManager", WsSecurityManager::class.java)
        if (StrUtil.isNotBlank(robotQQ) && AppConfig.getQQ() == robotQQ) {
            if (wsResult.msgType == MsgType.SEND_MSG) {
                val dataJson = wsResult.dataJson
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
        }
        return wsSecurityManager?.encryptMsgData(WsClientRes(200, "处理成功!").toJson())!!
    }
}