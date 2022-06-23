package cn.zjiali.robot.service

import cn.zjiali.robot.constant.ApiUrl
import cn.zjiali.robot.enums.GroupActionType
import cn.zjiali.robot.manager.RobotManager
import cn.zjiali.robot.model.response.ws.GroupAction
import cn.zjiali.robot.model.server.GroupMember
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import cn.zjiali.robot.util.PropertiesUtil
import com.google.inject.Inject
import com.google.inject.Singleton
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.contact.isAdministrator
import net.mamoe.mirai.containsGroup

/**
 *
 * @author zJiaLi
 * @since 2022-06-21 17:43
 */
@Singleton
class GroupActionService {

    @Inject
    private val robotManager: RobotManager? = null

    suspend fun doAction(groupAction: GroupAction) {
        val groupNumber = groupAction.groupNumber
        var group: Group? = null
        val bot = robotManager?.bot
        if (bot?.containsGroup(groupNumber!!) == true) {
            group = bot.getGroup(groupNumber!!)
        }
        if (group == null) return
        when (groupAction.actionType) {
            GroupActionType.PULL_MEMBER.ordinal -> {
                postGroupMember(group)
            }

            GroupActionType.MUTE_MEMBER.ordinal -> {
                val memberNumber = groupAction.memberNumber
                group.getMember(memberNumber!!)!!.mute(groupAction.muteTime!!)
            }

            GroupActionType.REMOVE_MEMBER.ordinal -> {
                group.getMember(groupAction.memberNumber!!)!!
                    .kick(groupAction.kickMessage!!, (groupAction.kickBlockFlag!! == 1))
            }
        }
    }

    private fun postGroupMember(group: Group) {
        val members: MutableList<GroupMember> = ArrayList()
        group.members.forEach { member ->
            run {
                members.add(
                    GroupMember(
                        member.id,
                        member.nick,
                        member.avatarUrl,
                        if (member.isAdministrator()) "1" else "0",
                        if (member.isMuted) "1" else "0",
                        member.muteTimeRemaining.toLong(),
                        group.id
                    )
                )
            }
        }
        HttpUtil.post(PropertiesUtil.getApiProperty(ApiUrl.POST_GROUP_MEMBER), JsonUtil.obj2str(members))
    }
}