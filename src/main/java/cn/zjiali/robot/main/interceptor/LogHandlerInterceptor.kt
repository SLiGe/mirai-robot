package cn.zjiali.robot.main.interceptor

import cn.zjiali.robot.annotation.Service
import cn.zjiali.robot.model.message.OutMessage
import cn.zjiali.robot.util.CommonLogger
import net.mamoe.mirai.event.events.MessageEvent

/**
 * 日志处理
 *
 * @author zJiaLi
 * @since 2021-09-04 18:14
 */
@Service
class LogHandlerInterceptor : HandlerInterceptor {

   private val logger: CommonLogger = CommonLogger(javaClass.simpleName)

    override fun preHandle(messageEvent: MessageEvent?): Boolean {
        logger.debug("处理消息内容: {}", messageEvent?.message?.contentToString())
        return super.preHandle(messageEvent)
    }

    override fun afterCompletion(messageEvent: MessageEvent?, outMessageList: List<OutMessage?>?) {
        if (outMessageList != null && outMessageList.isNotEmpty()) {
            for (outMessage in outMessageList) {
                logger.debug("输出消息内容:{}", outMessage?.finalMessage)
            }
        }
        super.afterCompletion(messageEvent, outMessageList)
    }

}