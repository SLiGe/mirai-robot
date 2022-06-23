package cn.zjiali.robot.model

import cn.zjiali.robot.config.Plugin

/**
 * @author zJiaLi
 * @since 2020-10-29 16:13
 */
class ApplicationConfig {
    /**
     * 是否启用总控制
     */
    var appEnable = 0
    var plugins: List<Plugin>? = null
    var qq: String? = null
    var password: String? = null

    /**
     * 是否服务端控制
     */
    var serverControl = 0

}