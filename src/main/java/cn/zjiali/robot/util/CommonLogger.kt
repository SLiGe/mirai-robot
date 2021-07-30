package cn.zjiali.robot.util

import cn.hutool.core.text.StrFormatter
import net.mamoe.mirai.utils.MiraiLogger

/**
 * @author zJiaLi
 * @since 2021-07-30 14:51
 */
class CommonLogger(identity: String?) {

    private val miraiLogger: MiraiLogger

    fun info(message: String?, vararg args: Any?) {
        miraiLogger.info(StrFormatter.format(message, *args))
    }

    fun debug(message: String?, vararg args: Any?) {
        miraiLogger.debug(StrFormatter.format(message, *args))
    }

    fun warning(message: String?, vararg args: Any?) {
        miraiLogger.warning(StrFormatter.format(message, *args))
    }

    init {
        miraiLogger = MiraiLogger.create(identity)
    }
}