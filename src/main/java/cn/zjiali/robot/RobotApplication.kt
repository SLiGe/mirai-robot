package cn.zjiali.robot

import cn.hutool.core.exceptions.ExceptionUtil
import cn.zjiali.robot.main.ApplicationBootStrap
import cn.zjiali.robot.manager.RobotManager
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.util.GuiceUtil
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.utils.LoggerAdapters.useLog4j2
import java.util.concurrent.CountDownLatch

/**
 * 应用启动类
 *
 * @author zJiaLi
 * @since 2020-10-29 11:09
 */
object RobotApplication {
    private val logger = CommonLogger(RobotApplication::class.java)

    @JvmField
    var initLatch = CountDownLatch(1)
    private fun init() {
        logger.info("====初始化配置中====")
        val startInitTime = System.currentTimeMillis()
        try {
            ApplicationBootStrap.getInstance().init()
            logger.info("====初始化配置完成==== 共耗时: {} ms ", System.currentTimeMillis() - startInitTime)
            logger.info("⭐⭐⭐⭐⭐⭐GitHub: https://github.com/SLiGe/mirai-robot ⭐⭐⭐⭐⭐⭐")
        } catch (e: Exception) {
            logger.error("====初始化配置出错,e: " + ExceptionUtil.stacktraceToString(e))
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        useLog4j2()
        init()
        val robotManager = GuiceUtil.getBean(RobotManager::class.java)
        val bot = robotManager.initBotBlocking()
        runBlocking { bot.join() }
    }

}
