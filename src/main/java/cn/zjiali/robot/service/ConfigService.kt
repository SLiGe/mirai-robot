package cn.zjiali.robot.service

import cn.zjiali.server.grpc.api.ConfigGrpc
import cn.zjiali.server.grpc.api.GetConfigRequest
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.inject.Inject
import com.google.inject.Singleton
import java.util.concurrent.TimeUnit

/**
 *
 * @author zJiaLi
 * @since 2023-02-09 14:49
 */
@Singleton
class ConfigService {

    @Inject
    private val configGrpc: ConfigGrpc.ConfigBlockingStub? = null

    private var cache: Cache<String, String> = CacheBuilder.newBuilder()
        .expireAfterWrite(6, TimeUnit.HOURS)
        .build()

    fun getConfig(configKey: String): String {
        return cache.get(configKey) {
            val config = configGrpc!!.getConfig(GetConfigRequest.newBuilder().setConfigKey(configKey).build())
            return@get config.configVal
        }
    }
}