package cn.zjiali.robot.util

import cn.hutool.core.text.StrFormatter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author zJiaLi
 * @since 2021-07-30 14:51
 */
class CommonLogger(clz: Class<*>) {

    private val logger: Logger

    fun info(message: String?, vararg args: Any?) {
        logger.info(StrFormatter.format(message, *args))
    }

    fun debug(message: String?, vararg args: Any?) {
        logger.debug(StrFormatter.format(message, *args))
    }

    fun warning(message: String?, vararg args: Any?) {
        logger.warn(StrFormatter.format(message, *args))
    }

    fun error(message: String?, vararg args: Any?) {
        logger.error(StrFormatter.format(message, *args))
    }

    init {
        logger = LoggerFactory.getLogger(clz)
    }
}