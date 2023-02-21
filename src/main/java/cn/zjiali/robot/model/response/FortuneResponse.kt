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
    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is FortuneResponse) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        if (status != other.status) return false
        val `this$message`: Any? = message
        val `other$message`: Any? = other.message
        if (if (`this$message` == null) `other$message` != null else `this$message` != `other$message`) return false
        val `this$dataResponse`: Any? = dataResponse
        val `other$dataResponse`: Any? = other.dataResponse
        return !if (`this$dataResponse` == null) `other$dataResponse` != null else `this$dataResponse` != `other$dataResponse`
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is FortuneResponse
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        result = result * PRIME + status
        val `$message`: Any? = message
        result = result * PRIME + (`$message`?.hashCode() ?: 43)
        val `$dataResponse`: Any? = dataResponse
        result = result * PRIME + (`$dataResponse`?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String {
        return "FortuneResponse(status=$status, message=$message, dataResponse=$dataResponse)"
    }

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
        override fun equals(o: Any?): Boolean {
            if (o === this) return true
            if (o !is DataResponse) return false
            val other = o
            if (!other.canEqual(this as Any)) return false
            val `this$id`: Any? = id
            val `other$id`: Any? = other.id
            if (if (`this$id` == null) `other$id` != null else `this$id` != `other$id`) return false
            val `this$fortuneSummary`: Any? = fortuneSummary
            val `other$fortuneSummary`: Any? = other.fortuneSummary
            if (if (`this$fortuneSummary` == null) `other$fortuneSummary` != null else `this$fortuneSummary` != `other$fortuneSummary`) return false
            val `this$luckyStar`: Any? = luckyStar
            val `other$luckyStar`: Any? = other.luckyStar
            if (if (`this$luckyStar` == null) `other$luckyStar` != null else `this$luckyStar` != `other$luckyStar`) return false
            val `this$signText`: Any? = signText
            val `other$signText`: Any? = other.signText
            if (if (`this$signText` == null) `other$signText` != null else `this$signText` != `other$signText`) return false
            val `this$unSignText`: Any? = unSignText
            val `other$unSignText`: Any? = other.unSignText
            return !if (`this$unSignText` == null) `other$unSignText` != null else `this$unSignText` != `other$unSignText`
        }

        protected fun canEqual(other: Any?): Boolean {
            return other is DataResponse
        }

        override fun hashCode(): Int {
            val PRIME = 59
            var result = 1
            val `$id`: Any? = id
            result = result * PRIME + (`$id`?.hashCode() ?: 43)
            val `$fortuneSummary`: Any? = fortuneSummary
            result = result * PRIME + (`$fortuneSummary`?.hashCode() ?: 43)
            val `$luckyStar`: Any? = luckyStar
            result = result * PRIME + (`$luckyStar`?.hashCode() ?: 43)
            val `$signText`: Any? = signText
            result = result * PRIME + (`$signText`?.hashCode() ?: 43)
            val `$unSignText`: Any? = unSignText
            result = result * PRIME + (`$unSignText`?.hashCode() ?: 43)
            return result
        }

        override fun toString(): String {
            return "FortuneResponse.DataResponse(id=$id, fortuneSummary=$fortuneSummary, luckyStar=$luckyStar, signText=$signText, unSignText=$unSignText)"
        }
    }
}