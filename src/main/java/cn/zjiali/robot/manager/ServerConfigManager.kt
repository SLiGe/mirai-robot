package cn.zjiali.robot.manager

import cn.zjiali.robot.constant.ApiUrl
import cn.zjiali.robot.model.server.AjaxResult
import cn.zjiali.robot.model.server.GroupPluginConfig
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.gson.reflect.TypeToken
import com.google.inject.Singleton
import java.util.concurrent.TimeUnit

/**
 *
 * @author zJiaLi
 * @since 2022-06-23 11:52
 */
@Singleton
class ServerConfigManager {

    private var cache: Cache<String, List<GroupPluginConfig>> = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build()


    fun getGroupPluginConfig(groupId: Long): List<GroupPluginConfig> {
        return cache.getIfPresent(groupId.toString())!!
    }

    fun pullServerConfig() {
        val groupPluginConfigJson = HttpUtil.get(ApiUrl.QUERY_GROUP_PLUGIN_CONFIG)
        val groupPluginConfig = JsonUtil.toObjByType<AjaxResult<List<GroupPluginConfig>>>(
            groupPluginConfigJson,
            object : TypeToken<AjaxResult<List<GroupPluginConfig>>>() {}.type
        )
        if (groupPluginConfig.success() && groupPluginConfig.data != null) {
            cache.invalidateAll()
            groupPluginConfig.data?.groupBy { e -> e.groupNumber }?.forEach { entry ->
                run {
                    cache.put(entry.key.toString(), entry.value)
                }
            }
        }
    }


}