package cn.zjiali.robot.manager

import cn.zjiali.robot.constant.ApiUrl
import cn.zjiali.robot.model.server.AjaxResult
import cn.zjiali.robot.model.server.GroupPluginConfig
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import cn.zjiali.robot.util.PropertiesUtil
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.google.inject.Singleton
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

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

    private val lock: Lock = ReentrantLock()


    fun getGroupPluginConfig(groupId: Long): List<GroupPluginConfig>? {
        lock.withLock { return cache.getIfPresent(groupId.toString()) }
    }

    fun pullServerConfig() {
        lock.withLock {
            val groupPluginConfigJson =
                HttpUtil.post(PropertiesUtil.getApiProperty(ApiUrl.QUERY_GROUP_PLUGIN_CONFIG), JsonObject())
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


}