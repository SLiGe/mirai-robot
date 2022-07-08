package cn.zjiali.robot.model.server

/**
 *
 *
 * @author zJiaLi
 * @since 2022-06-25 15:35
 */
data class GroupInfo(
    val groupName: String,
    val groupNumber: Long,
    val groupOwner: Long,
    val groupAvatar: String,
    val robotQq: Long
)
