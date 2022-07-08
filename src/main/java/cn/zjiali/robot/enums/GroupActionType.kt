package cn.zjiali.robot.enums

/**
 * 群组操作类型
 *
 * @author zJiaLi
 * @since 2022-06-21 22:14
 */
enum class GroupActionType {

    /**
     * 拉取群信息
     */
    PULL_GROUP,

    /**
     * 拉取成员
     */
    PULL_MEMBER,

    /**
     * 成员禁言
     */
    MUTE_MEMBER,

    /**
     * 移除成员
     */
    REMOVE_MEMBER,

}