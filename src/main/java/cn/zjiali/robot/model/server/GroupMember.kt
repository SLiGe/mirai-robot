package cn.zjiali.robot.model.server

/**
 *
 *
 * @author zJiaLi
 * @since 2022-06-21 22:28
 */
data class GroupMember(
    val memberQq: Long,
    val memberName: String,
    val memberAvatar: String,
    val adminFlag: String,
    val muteFlag: String,
    val muteTime: Long,
    val groupNumber: Long
)
