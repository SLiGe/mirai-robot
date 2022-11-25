package cn.zjiali.robot.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author zJiaLi
 * @since 2021-05-08 10:02
 */
@Serializable
class ServerResponse<T> {
    @SerialName(value = "code")
    var code = 0

    @SerialName(value = "msg")
    var msg: String? = null

    @SerialName(value = "data")
    var data: T? = null
        private set

    constructor() {}
    constructor(code: Int, msg: String?, data: T) {
        this.code = code
        this.msg = msg
        this.data = data
    }

    fun success(): Boolean {
        return code == 200
    }

    fun setData(data: T) {
        this.data = data
    }
}