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
     * 消息内容类型 text img
     */
    var contentType: String? = null

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

    /**
     * 发送群组集合
     */
    var sendGroupList: List<String>? = null

    /**
     * 接收人集合
     */
    var receiverList: List<String>? = null

    /**
     * 拓展数据
     */
    var extendData: MutableMap<String, String>? = null
}