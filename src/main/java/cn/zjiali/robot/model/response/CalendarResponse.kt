package cn.zjiali.robot.model.response

import com.google.gson.annotations.SerializedName

/**
 * 万年历响应实体
 *
 * {
 * "error_code": 0,
 * "reason": "Success",
 * "result": {
 * "data": {
 * "holiday": "元旦",//假日
 * "avoid": "破土.安葬.行丧.开生坟.",//忌
 * "animalsYear": "马",//属相
 * "desc": "1月1日至3日放假调休，共3天。1月4日（星期日）上班。",//假日描述
 * "weekday": "星期四",//周几
 * "suit": "订盟.纳采.造车器.祭祀.祈福.出行.安香.修造.动土.上梁.开市.交易.立券.移徙.入宅.会亲友.安机械.栽种.纳畜.造屋.起基.安床.造畜椆栖.",//宜
 * "lunarYear": "甲午年",//纪年
 * "lunar": "十一月十一",//农历
 * "year-month": "2015-1",//年份和月份
 * "date": "2015-1-1"//具体日期
 * }
 * }
 * }
 *
 * @author zJiaLi
 * @since 2021-04-04 10:57
 */
class CalendarResponse {
    /**
     * 假日
     */
    var holiday: String? = null

    /**
     * 忌
     */
    var avoid: String? = null

    /**
     * 属相
     */
    var animalsYear: String? = null

    /**
     * 假日描述
     */
    var holidayDesc: String? = null

    /**
     * 周几
     */
    var weekday: String? = null

    /**
     * 宜
     */
    var suit: String? = null

    /**
     * 纪年
     */
    var lunarYear: String? = null

    /**
     * 农历
     */
    var lunar: String? = null

    /**
     * 年份和月份
     */
    @SerializedName(value = "cyearMonth")
    var cyearMonth: String? = null

    /**
     * 具体日期
     */
    var specificDate: String? = null
}