package cn.zjiali.robot.model.response.ws

/**
 * @author zJiaLi
 * @since 2021-07-30 15:57
 */
class SenderMessageRes {
    /**
     * 接收人
     */
    var receiver: String? = null

    /**
     * 发送人
     */
    var sender: String? = null

    /**
     * 发送消息
     */
    var sendMessage: String? = null

    /**
     * 发送群组可空
     */
    var sendGroup: String? = null

    /**
     * 发送类型: 1 好友 2 群组@ 3 群组无@
     * @see cn.zjiali.robot.constant.MsgType
     */
    var sendFlag: Int? = null
}