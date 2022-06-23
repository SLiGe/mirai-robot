package cn.zjiali.robot.model.response.ws

/**
 *
 * @author zJiaLi
 * @since 2022-06-21 17:11
 */
class GroupAction {

    /**
     * 操作类型
     */
    var actionType: Int? = null

    /**
     * 禁言时间
     */
    var muteTime: Int? = null

    /**
     * 群号
     */
    var groupNumber: Long? = null

    /**
     * 成员号码
     */
    var memberNumber: Long? = null

    /**
     * 踢群提示
     */
    var kickMessage: String? = null

    /**
     * 踢群拉黑标志
     */
    var kickBlockFlag: Int? = null
}