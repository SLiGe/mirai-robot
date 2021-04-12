package cn.zjiali.robot.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * 万年历响应实体
 *
 * {
 *     "error_code": 0,
 *     "reason": "Success",
 *     "result": {
 *         "data": {
 *             "holiday": "元旦",//假日
 *             "avoid": "破土.安葬.行丧.开生坟.",//忌
 *             "animalsYear": "马",//属相
 *             "desc": "1月1日至3日放假调休，共3天。1月4日（星期日）上班。",//假日描述
 *             "weekday": "星期四",//周几
 *             "suit": "订盟.纳采.造车器.祭祀.祈福.出行.安香.修造.动土.上梁.开市.交易.立券.移徙.入宅.会亲友.安机械.栽种.纳畜.造屋.起基.安床.造畜椆栖.",//宜
 *             "lunarYear": "甲午年",//纪年
 *             "lunar": "十一月十一",//农历
 *             "year-month": "2015-1",//年份和月份
 *             "date": "2015-1-1"//具体日期
 *         }
 *     }
 * }
 *
 * @author zJiaLi
 * @since 2021-04-04 10:57
 */
public class CalendarResponse {

    /**
     * 假日
     */
    private String holiday;

    /**
     * 忌
     */
    private String avoid;

    /**
     * 属相
     */
    private String animalsYear;

    /**
     * 假日描述
     */
    private String desc;

    /**
     * 周几
     */
    private String weekday;

    /**
     * 宜
     */
    private String suit;

    /**
     * 纪年
     */
    private String lunarYear;

    /**
     * 农历
     */
    private String lunar;

    /**
     * 年份和月份
     */
    @SerializedName(value = "year-month")
    private String year_month;

    /**
     * 具体日期
     */
    private String date;

    public CalendarResponse() {
    }

    public CalendarResponse(String holiday, String avoid, String animalsYear, String desc, String weekday, String suit, String lunarYear, String lunar, String year_month, String date) {
        this.holiday = holiday;
        this.avoid = avoid;
        this.animalsYear = animalsYear;
        this.desc = desc;
        this.weekday = weekday;
        this.suit = suit;
        this.lunarYear = lunarYear;
        this.lunar = lunar;
        this.year_month = year_month;
        this.date = date;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getAvoid() {
        return avoid;
    }

    public void setAvoid(String avoid) {
        this.avoid = avoid;
    }

    public String getAnimalsYear() {
        return animalsYear;
    }

    public void setAnimalsYear(String animalsYear) {
        this.animalsYear = animalsYear;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getLunarYear() {
        return lunarYear;
    }

    public void setLunarYear(String lunarYear) {
        this.lunarYear = lunarYear;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getYear_month() {
        return year_month;
    }

    public void setYear_month(String year_month) {
        this.year_month = year_month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
