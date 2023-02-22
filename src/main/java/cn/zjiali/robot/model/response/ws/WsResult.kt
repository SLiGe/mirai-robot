package cn.zjiali.robot.model.response.ws

import kotlinx.serialization.Serializable

/**
 * @author zJiaLi
 * @since 2021-07-28 20:09
 */
@Serializable
class WsResult {
    /**
     * 消息类型
     */
    var msgType = 0

    /**
     * 传递数据
     */
    var dataJson: String? = null

    /**
     * 机器人QQ
     */
    var robotQQ: String? = null

    /**
     * 时间戳
     */
    var timestamp: Long = 0
}