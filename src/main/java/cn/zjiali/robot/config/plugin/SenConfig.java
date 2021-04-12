package cn.zjiali.robot.config.plugin;

import cn.zjiali.robot.annotation.Property;

/**
 * 一言插件配置
 *
 * @author zJiaLi
 * @since 2021-03-21 10:59
 */
public class SenConfig {

    @Property(name = "command")
    public static String sen_command;

    @Property(name = "enable")
    public static int sen_enable;

}
