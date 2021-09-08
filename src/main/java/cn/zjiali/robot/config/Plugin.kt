package cn.zjiali.robot.config

import java.util.HashMap

/**
 * @author zJiaLi
 * @since 2021-08-12 16:53
 */
class Plugin {
    /**
     * 插件名称
     */
    var name: String? = null

    /**
     * 插件代码
     */
    var code: String? = null

    /**
     * 触发命令
     */
    var command: String? = null

    /**
     * 是否启动 0 未启动 1已启动
     */
    var enable = 0

    /**
     * 消息模板
     */
    var template: String? = null

    /**
     * 忽略关键字
     */
    var ignoreKeyWords: String? = null

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