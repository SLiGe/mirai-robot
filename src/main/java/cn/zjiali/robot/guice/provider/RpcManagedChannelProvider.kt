package cn.zjiali.robot.guice.provider

import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.util.PropertiesUtil
import com.google.inject.Binder
import com.google.inject.Module
import com.google.inject.Provides
import com.google.inject.Singleton
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

/**
 * @author zJiaLi
 * @since 2023-02-09 14:24
 */
class RpcManagedChannelProvider : Module {
    private val logger = CommonLogger(RpcManagedChannelProvider::class.java)
    override fun configure(binder: Binder) {}

    @Provides
    @Singleton
    fun managedChannel(): ManagedChannel {
        val host = PropertiesUtil.getApplicationProperty("grpc.host")
        val port = PropertiesUtil.getApplicationProperty("grpc.port")
        logger.info("Grpc Host:{},Grpc Port:{}", host, port.toInt())
        return ManagedChannelBuilder.forAddress(host, port.toInt())
            .keepAliveWithoutCalls(true)
            .usePlaintext()
            .build()
    }
}