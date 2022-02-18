package cn.zjiali.robot.model.response.ws

import cn.zjiali.robot.util.JsonUtil

/**
 * @author zJiaLi
 * @since 2022-02-18 16:39
 */
class WsClientRes {
    var code: Int? = null
    var message: String? = null

    constructor(code: Int, message: String) {
        this.code = code
        this.message = message
    }

    fun toJson(): String {
        return JsonUtil.obj2str(this)
    }
}