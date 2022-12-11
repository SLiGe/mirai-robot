package cn.zjiali.robot.model.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 插件信息
 * @author zJiaLi
 * @since 2022-06-21 16:00
 */
@Serializable
data class PluginInfo(
    @SerialName(value = "pluginName")
    val pluginName: String,
    @SerialName(value = "pluginCode")
    val pluginCode: String,
    @SerialName(value = "pluginStatus")
    val pluginStatus: Int,
    @SerialName(value = "pluginClass")
    val pluginClass: String,
    @SerialName(value = "pluginConfigList")
    val pluginConfigList: MutableList<PluginConfig>
)

@Serializable
data class PluginConfig(
    @SerialName(value = "configKey")
    val configKey: String,
    @SerialName(value = "configValue")
    val configValue: String,
    @SerialName(value = "inMap")
    val inMap: String
)

@Serializable
data class GroupPluginInfo(
    @SerialName(value = "pluginCode")
    val pluginCode: String,
    @SerialName(value = "pluginStatus")
    val pluginStatus: Int,
)


