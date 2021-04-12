package cn.zjiali.robot.config.plugin;

import cn.zjiali.robot.annotation.Property;

/**
 * 老黄历配置类
 *
 * @author zJiaLi
 * @since 2021-04-04 20:37
 */
public class YellowCalendarConfig {

    @Property(name = "command")
    public static String yellow_calendar_command;

    @Property(name = "enable")
    public static int yellow_calendar_enable;

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
