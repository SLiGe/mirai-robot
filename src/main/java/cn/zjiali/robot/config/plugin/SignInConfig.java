package cn.zjiali.robot.config.plugin;

import cn.zjiali.robot.annotation.Property;

/**
 * 签到插件配置
 *
 * @author zJiaLi
 * @since 2021-03-21 11:01
 */
public class SignInConfig {

    @Property(name = "command")
    public static String sign_in_command;

    @Property(name = "enable")
    public static int sign_in_enable;

    @Property(name = "cur_level")
    public static String sign_in_cur_level;

    @Property(name = "day_sen")
    public static String sign_in_day_sen;

}
