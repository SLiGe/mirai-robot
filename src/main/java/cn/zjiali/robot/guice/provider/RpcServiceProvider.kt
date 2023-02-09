package cn.zjiali.robot.guice.provider

import cn.zjiali.robot.util.PropertiesUtil
import cn.zjiali.server.grpc.api.ConfigGrpc
import cn.zjiali.server.grpc.api.ConfigGrpc.ConfigBlockingStub
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
class RpcServiceProvider : Module {
    override fun configure(binder: Binder) {}
    @Provides
    @Singleton
    fun configBlockingStub(managedChannel: ManagedChannel?): ConfigBlockingStub {

        val host = PropertiesUtil.getApplicationProperty("grpc.host")
        val port = PropertiesUtil.getApplicationProperty("grpc.port")
        val build = ManagedChannelBuilder.forAddress(host, port.toInt())
            .keepAliveWithoutCalls(true)
            .usePlaintext()
            .build()
        return ConfigGrpc.newBlockingStub(build)
    }
}