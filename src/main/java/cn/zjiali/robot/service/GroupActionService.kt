package cn.zjiali.robot.service

import cn.zjiali.robot.constant.ApiUrl
import cn.zjiali.robot.enums.GroupActionType
import cn.zjiali.robot.manager.RobotManager
import cn.zjiali.robot.model.response.ws.GroupAction
import cn.zjiali.robot.model.server.GroupInfo
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
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *
 * @author zJiaLi
 * @since 2022-06-21 17:43
 */
@Singleton
class GroupActionService {

    @Inject
    private val robotManager: RobotManager? = null
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    suspend fun doAction(groupAction: GroupAction) {
        val groupNumber = groupAction.groupNumber
        var group: Group? = null
        val bot = robotManager?.bot
        if (groupAction.actionType != GroupActionType.PULL_GROUP.ordinal) {
            if (bot?.containsGroup(groupNumber!!) == true) {
                group = bot.getGroup(groupNumber!!)
            }
        }
        if (group == null && groupAction.actionType != GroupActionType.PULL_GROUP.ordinal) return
        when (groupAction.actionType) {
            GroupActionType.PULL_GROUP.ordinal -> {
                val groupList = ArrayList<GroupInfo>()
                bot?.groups!!.forEach { g ->
                    groupList.add(
                        GroupInfo(
                            g.name,
                            g.id,
                            g.owner.id,
                            g.avatarUrl,
                            bot.id
                        )
                    )
                }
                logger.debug("执行拉取群组信息操作,群信息数量:{}", groupList.size)
                postGroup(groupList)
            }

            GroupActionType.PULL_MEMBER.ordinal -> {
                postGroupMember(group!!)
            }

            GroupActionType.MUTE_MEMBER.ordinal -> {
                val memberNumber = groupAction.memberNumber
                group?.getMember(memberNumber!!)!!.mute(groupAction.muteTime!!)
            }

            GroupActionType.REMOVE_MEMBER.ordinal -> {
                group?.getMember(groupAction.memberNumber!!)!!
                    .kick(groupAction.kickMessage!!, (groupAction.kickBlockFlag!! == 1))
            }
        }
    }

    private fun postGroup(groupList: ArrayList<GroupInfo>) {
        HttpUtil.post(PropertiesUtil.getApiProperty(ApiUrl.POST_GROUP_INFO), JsonUtil.obj2str(groupList))
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
        logger.debug("执行拉取群成员信息操作,当前群:{} ,群成员数量:{}", group.id, members.size)
        HttpUtil.post(PropertiesUtil.getApiProperty(ApiUrl.POST_GROUP_MEMBER), JsonUtil.obj2str(members))
    }
}