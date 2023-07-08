package cn.zjiali.robot.model.response

/**
 * 运势实体
 *
 * @author zJiaLi
 * @since 2020-10-31 22:54
 */
open class FortuneResponse {
    var status = 0
    var message: String? = null
    var dataResponse: DataResponse? = null


    class DataResponse {
        var id: Long? = null

        /**
         * 运情总结
         */
        var fortuneSummary: String? = null

        /**
         * 幸运星
         */
        var luckyStar: String? = null

        /**
         * 签文
         */
        var signText: String? = null

        /**
         * 解签
         */
        var unSignText: String? = null

    }
}