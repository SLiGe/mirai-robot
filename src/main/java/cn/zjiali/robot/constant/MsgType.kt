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
     * 群组操作
     */
    const val GROUP_ACTION = 2

    /**
     * 配置操作
     */
    const val CONFIG_ACTION = 3

    /**
     * 刷新插件操作
     */
    const val REFRESH_PLUGIN_ACTION = 4

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

    /**
     * 发送群私聊
     */
    const val SEND_GROUP_PRIVATE_CHAT = 4

    /**
     * 发送群@全体成员
     */
    const val SEND_GROUP_AT_ALL = 5
}