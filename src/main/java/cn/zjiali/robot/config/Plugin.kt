package cn.zjiali.robot.config

import java.util.HashMap

/**
 * @author zJiaLi
 * @since 2021-08-12 16:53
 */
class Plugin {
    var name: String? = null
    var code: String? = null
    var command: String? = null
    var enable = 0
    var template: String? = null

    /**
     * 消息模板标识  0=无模板 1=单一模板 2=多个模板
     */
    var templateFlag: String? = null

    /**
     * 消息处理器
     */
    var handler: String? = null

    /**
     * 插件配置
     */
    var properties: HashMap<String, String>? = null


}