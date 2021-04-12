package cn.zjiali.robot.config.plugin;

import cn.zjiali.robot.annotation.Property;

/**
 * 万年历配置类
 *
 * @author zJiaLi
 * @since 2021-04-04 10:56
 */
public class CalendarConfig {

    @Property(name = "command")
    public static String calendar_command;

    @Property(name = "enable")
    public static int calendar_enable;

    @Property(name = "key")
    public static String calendar_key;

    @Property(name = "template")
    public static String template;
}
