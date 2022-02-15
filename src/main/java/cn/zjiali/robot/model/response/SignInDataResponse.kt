package cn.zjiali.robot.model.response

/**
 * 签到数据类
 *
 * @author zJiaLi
 * @since 2020-10-30 22:44
 */
class SignInDataResponse {
    var status: Int? = null
    var message: String? = null
    var dataResponse: DataResponse? = null

    class DataResponse {
        /**
         * QQ号
         */
        var qq: String? = null

        /**
         * 积分数
         */
        var points: Int? = null

        /**
         * 月签到天数
         */
        var monthDay: Int? = null

        /**
         * 总签到天数
         */
        var totalDay: Int? = null

        /**
         * 当前等级
         *
         * 2020/10/31 修改
         */
        var currentLevel: String? = null

        /**
         * 每日一句
         */
        var todayMsg: String? = null

        /**
         * 排名
         */
        var ranking = 0

        /**
         * 获得的积分
         */
        var getPoints: Int? = null
    }
}