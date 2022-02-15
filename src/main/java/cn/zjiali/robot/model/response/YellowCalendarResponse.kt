package cn.zjiali.robot.model.response

import com.google.gson.annotations.SerializedName

/**
 * 老黄历响应实体
 *
 * @author zJiaLi
 * @since 2021-04-04 20:33
 */
class YellowCalendarResponse {

    @SerializedName("yangli")
    var yangli: String? = null
    @SerializedName("yinli")
    var yinli: String? = null
    @SerializedName("wuxing")
    var wuxing: String? = null
    @SerializedName("chongsha")
    var chongsha: String? = null
    @SerializedName("baiji")
    var baiji: String? = null
    @SerializedName("jishen")
    var jishen: String? = null
    @SerializedName("yi")
    var yi: String? = null
    @SerializedName("xiongshen")
    var xiongshen: String? = null
    @SerializedName("ji")
    var ji: String? = null

    constructor() {}
    constructor(
        yangli: String?,
        yinli: String?,
        wuxing: String?,
        chongsha: String?,
        baiji: String?,
        jishen: String?,
        yi: String?,
        xiongshen: String?,
        ji: String?
    ) {
        this.yangli = yangli
        this.yinli = yinli
        this.wuxing = wuxing
        this.chongsha = chongsha
        this.baiji = baiji
        this.jishen = jishen
        this.yi = yi
        this.xiongshen = xiongshen
        this.ji = ji
    }
}