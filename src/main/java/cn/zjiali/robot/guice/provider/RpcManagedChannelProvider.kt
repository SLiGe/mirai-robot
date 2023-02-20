package cn.zjiali.robot.guice.provider

import cn.zjiali.robot.manager.ServerTokenManager
import cn.zjiali.robot.util.CommonLogger
import cn.zjiali.robot.util.PropertiesUtil
import com.google.inject.Binder
import com.google.inject.Module
import com.google.inject.Provides
import com.google.inject.Singleton
import io.grpc.ManagedChannel
import io.grpc.Metadata
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import io.grpc.stub.MetadataUtils
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

/**
 * @author zJiaLi
 * @since 2023-02-09 14:24
 */
class RpcManagedChannelProvider : Module {
    private val logger = CommonLogger(RpcManagedChannelProvider::class.java)
    override fun configure(binder: Binder) {}

    @Provides
    @Singleton
    fun managedChannel(serverTokenManager: ServerTokenManager): ManagedChannel {
        val serverEnv = System.getProperty("server.env")
        val managedChannel: ManagedChannel
        val metadata = Metadata()
        metadata.put(
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER),
            serverTokenManager.serverToken()
        )
        val rpcAddress: String
        val channelBuilder: NettyChannelBuilder
        if ("local" == serverEnv) {
            val host: String = PropertiesUtil.getApplicationProperty("grpc.local.host")
            val port: String = PropertiesUtil.getApplicationProperty("grpc.local.port")
            rpcAddress = "${host}:${port}"
            channelBuilder = NettyChannelBuilder.forTarget(rpcAddress).usePlaintext()
        } else {
            rpcAddress = PropertiesUtil.getApplicationProperty("grpc.address")
            channelBuilder = NettyChannelBuilder.forTarget(rpcAddress).useTransportSecurity()
        }
        logger.info("Grpc Address: $rpcAddress")
        managedChannel = channelBuilder.intercept(MetadataUtils.newAttachHeadersInterceptor(metadata))
            .keepAliveTime(Duration.ofMinutes(5).toNanos(), TimeUnit.NANOSECONDS)
            .keepAliveTimeout(Duration.of(20, ChronoUnit.SECONDS).toNanos(), TimeUnit.NANOSECONDS)
            .keepAliveWithoutCalls(true).build()
        managedChannel.getState(true)
        return managedChannel
    }


}