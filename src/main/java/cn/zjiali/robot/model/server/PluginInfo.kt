package cn.zjiali.robot.model.server

/**
 * 插件信息
 * @author zJiaLi
 * @since 2022-06-21 16:00
 */
data class PluginInfo(
    val pluginNane: String,
    val pluginCode: String,
    val pluginStatus: String,
    val pluginClass: String,
    val pluginConfigList: MutableList<PluginConfig>
)

data class PluginConfig(val configKey: String, val configValue: String, val inMap: String)
