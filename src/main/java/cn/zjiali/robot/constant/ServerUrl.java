package cn.zjiali.robot.constant;

import cn.zjiali.robot.annotation.Property;


/**
 * @author zJiaLi
 * @since 2020-10-29 21:29
 */
public class ServerUrl {

    /**
     * 一言接口
     */
    @Property(name = "SEN_URL")
    public static String SEN_URL;

    /**
     * 签到接口
     */
    @Property(name = "SIGN_IN_URL")
    public static String SIGN_IN_URL;

    /**
     * 获取签到数据接口
     */
    @Property(name = "SIGN_IN_DATA_URL")
    public static String SIGN_IN_DATA_URL;

    /**
     * 运势接口
     */
    @Property(name = "FORTUNE_URL")
    public static String FORTUNE_URL;

    /**
     * 老黄历
     */
    @Property(name = "juhe.laohuangli")
    public static String LAO_HUANG_LI_URL;


    /**
     * 万年历
     */
    @Property(name = "juhe.calendar")
    public static String CALENDAR_DAY_URL;

    /**
     * 新闻头条
     */
    @Property(name = "juhe.toutiao")
    public static String TOU_TIAO_URL;

    /**
     * 历史上的今天
     */
    @Property(name = "juhe.todayOnhistory")
    public static String TODAY_ON_HISTORY_URL;

    /**
     * 星座运势
     */
    @Property(name = "juhe.constellation")
    public static String CONSTELLATION_URL;

    /**
     * 成语接龙
     */
    @Property(name = "juhe.idiomJie")
    public static String IDIOM_JIE_URL;

    @Property(name = "saveData.api")
    public static String SAVE_DATA_URL;
}
