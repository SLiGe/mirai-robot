package cn.zjiali.robot.config.plugin;

import cn.zjiali.robot.annotation.Property;

/**
 * 运势插件配置
 *
 * @author zJiaLi
 * @since 2021-03-21 11:00
 */
public class FortuneConfig {

    @Property(name = "command")
    public static String fortune_command;
    @Property(name = "enable")
    public static int fortune_enable;
    @Property(name = "sign_text")
    public static String fortune_sign_text;
    @Property(name = "star_num")
    public static String fortune_star_num;
    @Property(name = "un_sign")
    public static String fortune_un_sign;
    @Property(name = "day_one")
    public static String fortune_day_one;
    @Property(name = "point")
    public static String fortune_point;

}
