package cn.zjiali.robot.config.plugin;

import cn.zjiali.robot.annotation.Property;

/**
 * 历史上的今天配置
 *
 * @author zJiaLi
 * @since 2021-04-03 21:48
 */
public class TodayOfHistoryConfig {

    @Property(name = "command")
    public static String today_of_history_command;

    @Property(name = "enable")
    public static int today_of_history_enable;

    /**
     * 聚合平台KEY
     */
    @Property(name = "key")
    public static String key;

    @Property(name = "url")
    public static String url;

    @Property(name = "template")
    public static String template;
}
