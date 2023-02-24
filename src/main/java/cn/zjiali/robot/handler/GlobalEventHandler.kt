package cn.zjiali.robot.handler

import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent

/**
 * 事件处理
 *
 * @author zJiaLi
 * @since 2023-02-24 17:35
 */
interface GlobalEventHandler {
    /**
     * 处理机器人被邀请入群事件
     */
    suspend fun handleBotInvitedJoinGroupRequestEvent(event: BotInvitedJoinGroupRequestEvent?)
}