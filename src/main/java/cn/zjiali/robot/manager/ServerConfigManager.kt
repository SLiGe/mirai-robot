package cn.zjiali.robot.manager

import cn.zjiali.server.grpc.api.group.ConfigResponse
import cn.zjiali.server.grpc.api.group.GroupPluginConfigGrpc.GroupPluginConfigBlockingStub
import cn.zjiali.server.grpc.api.group.QueryConfigRequest
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.inject.Inject
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

    private var cache: Cache<String, List<ConfigResponse>> = CacheBuilder.newBuilder()
        .expireAfterWrite(12, TimeUnit.HOURS)
        .build()

    private val lock: Lock = ReentrantLock()

    @Inject
    private val groupPluginConfigBlockingStub: GroupPluginConfigBlockingStub? = null


    fun getGroupPluginConfig(groupId: Long): List<ConfigResponse>? {
        lock.withLock { return cache.getIfPresent(groupId.toString()) }
    }

    fun pullServerConfig() {
        lock.withLock {
            val groupPluginConfig = groupPluginConfigBlockingStub!!.queryConfig(QueryConfigRequest.newBuilder().build())
            if (groupPluginConfig != null) {
                cache.invalidateAll()
                groupPluginConfig.configListList?.groupBy { e -> e.groupNumber }?.forEach { entry ->
                    run {
                        cache.put(entry.key.toString(), entry.value)
                    }
                }
            }
        }
    }


}