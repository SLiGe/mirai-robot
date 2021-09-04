package cn.zjiali.robot.main.interceptor

import kotlin.Throws
import java.lang.Exception
import cn.zjiali.robot.model.message.OutMessage
import net.mamoe.mirai.event.events.MessageEvent

/**
 * 插件处理拦截器
 * @author zJiaLi
 * @since 2021-06-14 12:25
 */
interface HandlerInterceptor {

    /**
     * 拦截前置处理
     * @param messageEvent 消息事件
     */
    @Throws(Exception::class)
    fun preHandle(messageEvent: MessageEvent?): Boolean {
        return true
    }

    /**
     * 拦截处理器的执行
     * @param messageEvent 消息事件
     */
    @Throws(Exception::class)
    fun postHandle(messageEvent: MessageEvent?) {
    }

    /**
     * 消息处理完成后的回调 - 获得输出消息后
     * @param messageEvent 消息事件
     * @param outMessageList 输出消息列表
     */
    @Throws(Exception::class)
    fun afterCompletion(messageEvent: MessageEvent?, outMessageList: List<OutMessage?>?) {
    }
}