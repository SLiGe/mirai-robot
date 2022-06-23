package cn.zjiali.robot.model.server

/**
 *
 * @author zJiaLi
 * @since 2022-06-23 11:55
 */
class GroupPluginConfig {

    /**
     * 插件编码
     */
    var pluginCode: String? = null

    /**
     * 配置名
     */
    var configName: String? = null

    /**
     * 配置键
     */
    var configKey: String? = null

    /**
     * 配置值
     */
    var configValue: String? = null

    /**
     * 群号
     */
    var groupNumber: Long? = null

    /**
     * 是否在properties中
     */
    var inMap: Int? = null


}