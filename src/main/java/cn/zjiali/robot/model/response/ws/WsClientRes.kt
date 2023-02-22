package cn.zjiali.robot.model.response.ws

import cn.zjiali.robot.util.JsonUtil

/**
 * @author zJiaLi
 * @since 2022-02-18 16:39
 */
class WsClientRes(code: Int, message: String) {
    var code: Int? = code
    var message: String? = message

    fun toJson(): String {
        return JsonUtil.obj2str(this)
    }
}