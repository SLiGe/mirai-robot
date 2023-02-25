package cn.zjiali.robot.service

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.Constants
import cn.zjiali.robot.enums.GroupActionType
import cn.zjiali.robot.manager.RobotManager
import cn.zjiali.robot.model.response.ws.GroupAction
import cn.zjiali.server.grpc.api.group.*
import com.google.common.collect.Lists
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

    @Inject
    private val groupGrpc: GroupGrpc.GroupBlockingStub? = null
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
        if (group == null) return
        when (groupAction.actionType) {
            GroupActionType.PULL_GROUP.ordinal -> {
                val groupList = ArrayList<GroupInfo>()
                bot?.groups!!.forEach { g ->
                    groupList.add(
                        GroupInfo.newBuilder()
                            .setGroupName(g.name)
                            .setGroupNumber(g.id)
                            .setGroupOwner(g.owner.id)
                            .setGroupAvatar(g.avatarUrl)
                            .build()
                    )
                }
                logger.debug("执行拉取群组信息操作,群信息数量:{}", groupList.size)
                postGroup(groupList)
            }

            GroupActionType.PULL_MEMBER.ordinal -> {
                postGroupMember(group)
            }

            GroupActionType.MUTE_MEMBER.ordinal -> {
                val memberNumber = groupAction.memberNumber
                group.getMember(memberNumber!!)!!.mute(groupAction.muteTime!! * 60)
            }

            GroupActionType.REMOVE_MEMBER.ordinal -> {
                group.getMember(groupAction.memberNumber!!)!!
                    .kick(groupAction.kickMessage!!, (groupAction.kickBlockFlag!! == 1))
            }
        }
    }

    private fun postGroup(groupList: ArrayList<GroupInfo>) {
        Lists.partition(groupList, 10).forEach {
            groupGrpc!!.postGroup(PostGroupRequest.newBuilder().addAllGroup(it).setRobotQq(AppConfig.qq()).build())
        }
    }

    private fun postGroupMember(group: Group) {
        val members: MutableList<GroupMemberInfo> = ArrayList()
        group.members.forEach { member ->
            run {
                members.add(
                    GroupMemberInfo.newBuilder()
                        .setMemberQq(member.id)
                        .setMemberName(member.nick)
                        .setMemberAvatar(member.avatarUrl)
                        .setAdminFlag(if (member.isAdministrator()) Constants.Y else Constants.N)
                        .setMuteFlag(if (member.isMuted) Constants.Y else Constants.N)
                        .setMuteTime(member.muteTimeRemaining.toLong())
                        .build()
                )
            }
        }
        Lists.partition(members, 10).forEach {
            groupGrpc!!.postGroupMember(
                PostGroupMemberRequest.newBuilder().setRobotQq(AppConfig.qq())
                    .addAllGroupMember(it)
                    .setGroupNumber(group.id).build()
            )
        }
        logger.debug("执行拉取群成员信息操作,当前群:{} ,群成员数量:{}", group.id, members.size)
    }
}