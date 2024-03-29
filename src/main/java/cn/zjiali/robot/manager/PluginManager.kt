package cn.zjiali.robot.manager

import cn.hutool.core.lang.ParameterizedTypeImpl
import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.config.Plugin
import cn.zjiali.robot.constant.ApiUrl
import cn.zjiali.robot.constant.ConfigKey
import cn.zjiali.robot.constant.Constants
import cn.zjiali.robot.constant.PluginProperty
import cn.zjiali.robot.handler.MessageEventHandler
import cn.zjiali.robot.model.response.ServerResponse
import cn.zjiali.robot.model.server.PluginInfo
import cn.zjiali.robot.util.*
import com.google.inject.Inject
import com.google.inject.Singleton
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

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

    fun getTemplate(pluginCode: String, templateCode: String?, groupId: Long, senderId: Long): String {
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
        logger.debug(
            "getConfigVal pluginCode:{},configKey:{},inMap:{},groupId:{},senderId,{}",
            pluginCode,
            configKey,
            inMap,
            groupId,
            senderId
        )
        val groupPluginConfig = serverConfigManager?.getGroupPluginConfig(groupId)
        if (groupPluginConfig.isNullOrEmpty()) return null
        return groupPluginConfig.filter { config -> config.pluginCode.equals(pluginCode) }
            .filter { config -> (config.inMap == 1) == inMap }
            .filter { config -> config.configKey.equals(configKey) }
            .map { config -> config.configValue }
            .getOrNull(0)
    }

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * 刷新插件
     */
    fun refreshPlugin() {
        val pluginInfoJson = HttpUtil.get(PropertiesUtil.getApiProperty(ApiUrl.QUERY_PLUGIN_INFO))
        if (pluginInfoJson.isNotEmpty()) {
            val response =
                json.decodeFromString<ServerResponse<MutableList<PluginInfo>>>(pluginInfoJson)
            if (response.data!!.isNotEmpty()) {
                val msgHandlers = AppConfig.getMsgHandlers()
                msgHandlers.clear()
                val pluginList = response.data!!.map {
                    val plugin = Plugin()
                    plugin.code = it.pluginCode
                    plugin.name = it.pluginName
                    plugin.handler = it.pluginClass
                    val pluginHandler = Class.forName(plugin.handler).getDeclaredConstructor().newInstance()
                    injectClassField(pluginHandler)
                    msgHandlers.add((pluginHandler as MessageEventHandler?))
                    val properties = HashMap<String, String>()
                    it.pluginConfigList.forEach { pluginConfig ->
                        run {
                            val configKey = pluginConfig.configKey
                            val configValue = pluginConfig.configValue
                            when (configKey) {
                                "command" -> plugin.command = configValue
                                "enable" -> plugin.enable = configValue.toInt()
                                "templateFlag" -> plugin.templateFlag = configValue
                                "template" -> plugin.template = configValue
                            }
                            properties[configKey] = configValue
                        }
                    }
                    fillDefaultNullFieldVal(plugin)
                    plugin.properties = properties
                    plugin
                }
                AppConfig.getApplicationConfig().plugins = pluginList
            }
        }
    }

    private fun fillDefaultNullFieldVal(plugin: Plugin) {
        if (plugin.templateFlag.isNullOrBlank()) {
            plugin.templateFlag = Constants.N
        }
    }

    fun injectClassField(obj: Any) {
        obj.javaClass.declaredFields.forEach {
            if (it.isAnnotationPresent(Inject::class.java)) {
                it.isAccessible = true
                when (it.type) {
                    List::class.java -> {
                        it.set(
                            obj,
                            GuiceUtil.getMultiBean((it.genericType as ParameterizedTypeImpl).actualTypeArguments[0].javaClass)
                        )
                    }

                    Set::class.java -> {
                        val list =
                            GuiceUtil.getMultiBean((it.genericType as ParameterizedTypeImpl).actualTypeArguments[0].javaClass)
                        it.set(
                            obj,
                            list.toSet()
                        )
                    }

                    else -> {
                        it.set(obj, GuiceUtil.getBean(it.type))
                    }
                }
            }
        }
    }

}