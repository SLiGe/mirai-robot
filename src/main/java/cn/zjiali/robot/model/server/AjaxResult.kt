package cn.zjiali.robot.model.server

/**
 * @author zJiaLi
 * @since 2022-06-23 12:54
 */
class AjaxResult<T> {
    var code: String? = null
    var msg: String? = null
    var token: String? = null
    var data: T? = null
        private set

    fun setData(data: T) {
        this.data = data
    }

    fun success(): Boolean {
        return this.code.equals("200")
    }
}