package cn.zjiali.robot.handler

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.MsgType
import cn.zjiali.robot.enums.GroupActionType
import cn.zjiali.robot.main.websocket.WebSocketManager
import cn.zjiali.robot.manager.RobotManager
import cn.zjiali.robot.model.response.ws.GroupAction
import cn.zjiali.robot.service.GroupActionService
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.util.JsonUtil
import com.google.inject.Inject
import com.google.inject.Singleton
import kotlinx.coroutines.*
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent

/**
 * @author zJiaLi
 * @since 2023-02-24 17:36
 */
@Singleton
class ServerGlobalEventHandler(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) : GlobalEventHandler {

    private val logger: CommonLogger = CommonLogger(javaClass)


    @Inject
    private val robotManager: RobotManager? = null

    @Inject
    private val webSocketManager: WebSocketManager? = null

    @Inject
    private val groupActionService: GroupActionService? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun handleBotInvitedJoinGroupRequestEvent(event: BotInvitedJoinGroupRequestEvent?) {
        GlobalScope.launch(dispatcher) {
            event!!.accept()
            val param = HashMap<String, Any>()
            param["robot"] = AppConfig.getQQ()
            param["msgType"] = MsgType.INVITED_JOINED_GROUP_EVENT_MSG
            val messageBody = HashMap<String, Any>()
            messageBody["invitor"] = event.invitorId
            val groupId = event.groupId
            messageBody["group"] = groupId
            messageBody["groupName"] = event.groupName
            val group = robotManager!!.bot!!.getGroup(groupId)
            messageBody["groupOwner"] = group!!.owner
            param["messageBody"] = messageBody
            val requestJson = JsonUtil.obj2str(param)
            webSocketManager!!.sendText(requestJson)
            val groupAction = GroupAction()
            groupAction.actionType = GroupActionType.PULL_MEMBER.ordinal
            groupAction.groupNumber = groupId
            groupActionService!!.doAction(groupAction)
            logger.info("send event msg:{}", requestJson)
        }
    }
}