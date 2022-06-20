package cn.zjiali.robot.main.interceptor

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.content

/**
 * 服务端群消息拦截器
 *
 * @author zJiaLi
 * @since 2022-06-20 22:46
 */
class ServerGroupHandlerInterceptor : HandlerInterceptor {
    override fun preHandle(messageEvent: MessageEvent?): Boolean {
        if (messageEvent is GroupMessageEvent) {
            val groupId = messageEvent.group.id
            val id = messageEvent.sender.id
            val content = messageEvent.message.content

        }
        return super.preHandle(messageEvent)
    }
}