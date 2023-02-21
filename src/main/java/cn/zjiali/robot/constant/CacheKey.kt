package cn.zjiali.robot.constant

/**
 *
 *
 * @author zJiaLi
 * @since 2022-02-20 14:44
 */
object CacheKey {

    /**
     * WebSocket 消息加密密钥
     */
    const val MESSAGE_ENCRYPT_KEY = "robot.websocket.message.encrypt"

    /**
     * WebSocket 连接验证密钥
     */
    const val WS_VERIFY_KEY = "robot.websocket.connect.verify"

    /**
     * 回复消息黑名单
     */
    const val REPLY_BLACK_LIST_KEY = "robot.reply.blacklist"
}