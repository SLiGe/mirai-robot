package cn.zjiali.robot.main.interceptor

import cn.zjiali.robot.constant.CacheKey
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.service.DictService
import cn.zjiali.robot.util.PropertiesUtil
import com.google.inject.Inject
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
class ReplyBlacklistHandlerInterceptor : HandlerInterceptor {
    private val commonLogger = CommonLogger(javaClass)

    @Inject
    private val dictService: DictService? = null
    private var replyBlacklist: String? = null
    private val queryReplyBlacklistParamMap: MutableMap<String, Any> = mutableMapOf()

    @Throws(Exception::class)
    override fun preHandle(messageEvent: MessageEvent?): Boolean {
        val serverReplyBlacklistStr =
            dictService!!.getDictVal(CacheKey.REPLY_BLACK_LIST_KEY, queryReplyBlacklistParamMap)
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