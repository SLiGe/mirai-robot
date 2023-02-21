package cn.zjiali.robot.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author zJiaLi
 * @since 2021-05-08 10:02
 */
@Serializable
class RobotBaseResponse<T> {
    @SerialName(value = "status")
    var status = 0

    @SerialName(value = "message")
    var message: String? = null

    @SerialName(value = "data")
    var data: T? = null
        private set

    constructor()
    constructor(status: Int, message: String?, data: T) {
        this.status = status
        this.message = message
        this.data = data
    }

    fun success(): Boolean {
        return status == 200
    }

    fun setData(data: T) {
        this.data = data
    }
}