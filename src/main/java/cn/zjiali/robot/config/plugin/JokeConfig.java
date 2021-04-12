package cn.zjiali.robot.config.plugin;

import cn.zjiali.robot.annotation.Property;

/**
 * 笑话配置类
 *
 * @author zJiaLi
 * @since 2021-04-04 10:41
 */
public class JokeConfig {

    @Property(name = "command")
    public static String joke_command;

    @Property(name = "enable")
    public static int joke_enable;

    @Property(name = "key")
    public static String joke_key;

    @Property(name = "url")
    public static String url;

    @Property(name = "template")
    public static String template;

}

