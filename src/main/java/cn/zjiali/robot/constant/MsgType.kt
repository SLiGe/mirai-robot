package cn.zjiali.robot.constant

/**
 * @author zJiaLi
 * @since 2021-07-30 15:47
 */
object MsgType {
    /**
     * 连接消息
     */
    const val CONNECT = 0

    /**
     * 发送消息
     */
    const val SEND_MSG = 1

    /**
     * 发送好友消息
     */
    const val SEND_FRIEND_MSG = 1

    /**
     * 发送群组消息
     */
    const val SEND_GROUP_MSG = 2

    /**
     * 发送群组@消息
     */
    const val SEND_GROUP_AT_MSG = 3
}