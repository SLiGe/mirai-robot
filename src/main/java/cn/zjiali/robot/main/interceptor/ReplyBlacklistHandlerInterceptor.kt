package cn.zjiali.robot.main.interceptor

import cn.zjiali.robot.annotation.Autowired
import cn.zjiali.robot.annotation.Service
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.service.DictService
import cn.zjiali.robot.util.PropertiesUtil
import com.google.common.collect.Maps
import net.mamoe.mirai.event.events.MessageEvent
import kotlin.Throws
import java.lang.Exception
import java.io.IOException

/**
 * 回复黑名单拦截器
 *
 * @author zJiaLi
 * @since 2021-06-14 12:15
 */
@Service
class ReplyBlacklistHandlerInterceptor : HandlerInterceptor {
    private val commonLogger = CommonLogger(ReplyBlacklistHandlerInterceptor::class.java.simpleName)

    @Autowired
    private val dictService: DictService? = null
    private var replyBlacklist: String? = null
    private val queryReplyBlacklistParamMap: MutableMap<String, Any> = Maps.newHashMap()

    @Throws(Exception::class)
    override fun preHandle(messageEvent: MessageEvent?): Boolean {
        val serverReplyBlacklistStr = dictService!!.getDictVal(queryReplyBlacklistParamMap)
        val qq = messageEvent?.sender?.id.toString()
        if (serverReplyBlacklistStr.contains(qq) || replyBlacklist!!.contains(qq)) {
            commonLogger.info("QQ: {} in reply blacklist!", qq)
            return false
        }
        return super.preHandle(messageEvent)
    }

    init {
        queryReplyBlacklistParamMap["dictCode"] = "QrReplyBlacklist"
        queryReplyBlacklistParamMap["dictTypeCode"] = "D00001"
        try {
            replyBlacklist = PropertiesUtil.getApplicationProperty("robot.reply.blacklist")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}