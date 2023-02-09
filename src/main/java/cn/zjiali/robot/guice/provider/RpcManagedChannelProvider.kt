package cn.zjiali.robot.guice.provider

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
    override fun configure(binder: Binder) {}
    @Provides
    @Singleton
    fun managedChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress("localhost", 18090)
            .usePlaintext()
            .build()
    }
}