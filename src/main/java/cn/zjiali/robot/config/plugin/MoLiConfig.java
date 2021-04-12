package cn.zjiali.robot.config.plugin;

import cn.zjiali.robot.annotation.Property;

/**
 * @author zJiaLi
 * @since 2021-04-08 11:47
 */
public class MoLiConfig {

    @Property(name = "chatGroupAt")
    public static String chatGroupAt;
    
    @Property(name = "jokeCommand")
    public static String jokeCommand;
    @Property(name = "jokeEnable")
    public static String jokeEnable;
    @Property(name = "jokeTemplate")
    public static String jokeTemplate;
    
    @Property(name = "gylqCommand")
    public static String gylqCommand;
    @Property(name = "gylqEnable")
    public static String gylqEnable;
    @Property(name = "gylqTemplate")
    public static String gylqTemplate;
    
    @Property(name = "yllqCommand")
    public static String yllqCommand;
    @Property(name = "yllqEnable")
    public static String yllqEnable;
    @Property(name = "yllqTemplate")
    public static String yllqTemplate;
    
    @Property(name = "csylqCommand")
    public static String csylqCommand;
    @Property(name = "csylqEnable")
    public static String csylqEnable;
    @Property(name = "csylqTemplate")
    public static String csylqTemplate;
    
    
    @Property(name = "limit")
    public static String limit;
    @Property(name = "api_key")
    public static String apiKey;
    @Property(name = "api_secret")
    public static String apiSecret;
    @Property(name = "type")
    public static String type;
    @Property(name = "url")
    public static String url;

}
