package cn.zjiali.robot.util

import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.config.Plugin
import cn.zjiali.robot.config.PluginTemplate
import cn.zjiali.robot.constant.PluginProperty

/**
 * @author zJiaLi
 * @since 2021-08-13 14:11
 */
object PluginConfigUtil {

    @JvmStatic
    fun getConfigVal(pluginCode: String, key: String): String? {
        val pluginI = getPlugin(pluginCode)
        val properties = pluginI.properties
        return properties?.get(key)
    }

    fun getPlugin(pluginCode: String): Plugin {
        return AppConfig.getApplicationConfig().plugins!!.stream().filter { plugin: Plugin -> pluginCode == plugin.code }
            .findFirst().orElseThrow {
                println("插件: $pluginCode 未找到!")
                NullPointerException("未找到插件!")
            }
    }

    @JvmStatic
    fun getTemplate(pluginCode: String): String {
        return getPlugin(pluginCode).template.orEmpty()
    }

    @JvmStatic
    fun getTemplate(pluginCode: String, templateCode: String?): String {
        val plugin = getPlugin(pluginCode)
        return if ("1" == plugin.templateFlag)
            getPlugin(pluginCode).template.orEmpty()
        else
            PluginTemplate.getInstance().getTemplate(templateCode)
    }

    @JvmStatic
    fun getApiKey(pluginCode: String): String {
        return getConfigVal(pluginCode, PluginProperty.API_KEY).orEmpty()
    }

    fun getApiSecret(pluginCode: String): String {
        return getConfigVal(pluginCode, PluginProperty.API_SECRET).orEmpty()
    }

    @JvmStatic
    fun getApiURL(pluginCode: String): String {
        return getConfigVal(pluginCode, PluginProperty.API_URL).orEmpty()
    }

    @JvmStatic
    fun getCommand(pluginCode: String): String {
        return getPlugin(pluginCode).command.orEmpty()
    }

    @JvmStatic
    fun getEnable(pluginCode: String): Int {
        return getPlugin(pluginCode).enable
    }

    @JvmStatic
    fun getIgnoreWords(pluginCode: String): ArrayList<String> {
        val ignoreKeyWords = getPlugin(pluginCode).ignoreKeyWords
        if (ObjectUtil.isNotNullOrEmpty(ignoreKeyWords)) {
           return ignoreKeyWords?.split(",")?.toList() as ArrayList<String>
        }
        return ArrayList()
    }
}