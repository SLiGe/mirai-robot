package cn.zjiali.robot.manager

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.constant.ConfigKey
import cn.zjiali.robot.constant.PluginProperty
import cn.zjiali.robot.util.ObjectUtil
import cn.zjiali.robot.util.PluginConfigUtil
import com.google.inject.Inject
import com.google.inject.Singleton
import java.util.*

/**
 *
 * @author zJiaLi
 * @since 2022-06-23 15:08
 */
@Singleton
class PluginManager {

    @Inject
    private var serverConfigManager: ServerConfigManager? = null

    fun getConfigVal(pluginCode: String, key: String, groupId: Long, senderId: Long): String? {
        if (AppConfig.serverControl() && groupId > 0) {
            val configVal = getConfigVal(pluginCode, key, true, groupId, senderId)
            return if (configVal.isNullOrBlank()) {
                PluginConfigUtil.getConfigVal(pluginCode, key)
            } else return configVal
        } else return PluginConfigUtil.getConfigVal(pluginCode, key)
    }

    private fun getTemplate(pluginCode: String, groupId: Long, senderId: Long): String {
        return if (AppConfig.serverControl() && groupId > 0) Optional.ofNullable(
            getConfigVal(
                pluginCode,
                ConfigKey.TEMPLATE,
                false,
                groupId,
                senderId
            )
        )
            .orElse(PluginConfigUtil.getTemplate(pluginCode))
        else return PluginConfigUtil.getTemplate(pluginCode)
    }

    fun getTemplate(pluginCode: String, templateCode: String?, groupId: Long, senderId: Long): String? {
        if (AppConfig.serverControl() && groupId > 0) {
            val templateFlag =
                Optional.ofNullable(getConfigVal(pluginCode, ConfigKey.TEMPLATE_FLAG, false, groupId, senderId))
                    .orElse(PluginConfigUtil.getPlugin(pluginCode).templateFlag)
            val template: String? = if ("1" == templateFlag) {
                getTemplate(pluginCode, groupId, senderId)
            } else {
                getConfigVal(pluginCode, templateCode!!, true, groupId, senderId)
            }
            if (template.isNullOrEmpty()) {
                return PluginConfigUtil.getTemplate(pluginCode, templateCode)
            }
            return template
        } else {
            return PluginConfigUtil.getTemplate(pluginCode, templateCode)
        }
    }

    fun getApiURL(pluginCode: String, groupId: Long, senderId: Long): String {
        return if (AppConfig.serverControl() && groupId > 0) Optional.ofNullable(
            getConfigVal(
                pluginCode,
                PluginProperty.API_URL,
                false,
                groupId,
                senderId
            )
        )
            .orElse(PluginConfigUtil.getConfigVal(pluginCode, PluginProperty.API_URL).orEmpty())
        else PluginConfigUtil.getConfigVal(pluginCode, PluginProperty.API_URL).orEmpty()
    }

    fun getCommand(pluginCode: String, groupId: Long, senderId: Long): String {
        return if (AppConfig.serverControl() && groupId > 0) Optional.ofNullable(
            getConfigVal(
                pluginCode,
                ConfigKey.COMMAND,
                false,
                groupId,
                senderId
            )
        )
            .orElse(PluginConfigUtil.getPlugin(pluginCode).command.orEmpty())
        else PluginConfigUtil.getPlugin(pluginCode).command.orEmpty()
    }

    fun getIgnoreWords(pluginCode: String, groupId: Long, senderId: Long): ArrayList<String> {
        val ignoreKeyWords = if (AppConfig.serverControl() && groupId > 0)
            Optional.ofNullable(getConfigVal(pluginCode, ConfigKey.IGNORE_KEY_WORDS, false, groupId, senderId))
                .orElse(PluginConfigUtil.getPlugin(pluginCode).ignoreKeyWords)
        else PluginConfigUtil.getPlugin(pluginCode).ignoreKeyWords
        if (ObjectUtil.isNotNullOrEmpty(ignoreKeyWords)) {
            return ignoreKeyWords?.split(",")?.toList() as ArrayList<String>
        }
        return ArrayList()
    }


    private fun getConfigVal(
        pluginCode: String,
        configKey: String,
        inMap: Boolean,
        groupId: Long,
        senderId: Long
    ): String? {
        val groupPluginConfig = serverConfigManager?.getGroupPluginConfig(groupId)
        if (groupPluginConfig.isNullOrEmpty()) return null
        return groupPluginConfig.filter { config -> config.pluginCode.equals(pluginCode) }
            .filter { config -> (config.inMap == 1) == inMap }
            .filter { config -> config.configKey.equals(configKey) }
            .map { config -> config.configValue }
            .getOrNull(0)!!
    }

}