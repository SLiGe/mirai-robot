package cn.zjiali.robot;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.manager.RobotManager;
import cn.zjiali.robot.util.CommonLogger;
import cn.zjiali.robot.util.GuiceUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.LoggerAdapters;

import java.util.concurrent.CountDownLatch;


/**
 * 应用启动类
 *
 * @author zJiaLi
 * @since 2020-10-29 11:09
 */
public class RobotApplication {

    public static final CountDownLatch initLatch = new CountDownLatch(1);
    private static final CommonLogger logger = new CommonLogger(RobotApplication.class);

    private static void init() {
        logger.info("====初始化配置中====");
        long startInitTime = System.currentTimeMillis();
        try {
            ApplicationBootStrap.getInstance().init();
            logger.info("====初始化配置完成==== 共耗时: {} ms ", (System.currentTimeMillis() - startInitTime));
            logger.info("⭐⭐⭐⭐⭐⭐GitHub: https://github.com/SLiGe/mirai-robot ⭐⭐⭐⭐⭐⭐");
        } catch (Exception e) {
            logger.error("====初始化配置出错,e: " + ExceptionUtil.stacktraceToString(e));
        }
    }

    public static void main(String[] args) {
        LoggerAdapters.useLog4j2();
        init();
        final RobotManager robotManager = GuiceUtil.getBean(RobotManager.class);
        Bot bot = robotManager.initBotClocking();
        bot.join();
    }

}
