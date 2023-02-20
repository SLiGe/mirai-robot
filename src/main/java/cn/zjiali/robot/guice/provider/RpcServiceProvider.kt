package cn.zjiali.robot.guice.provider

import cn.zjiali.server.grpc.api.ConfigGrpc
import cn.zjiali.server.grpc.api.ConfigGrpc.ConfigBlockingStub
import cn.zjiali.server.grpc.api.group.GroupPluginConfigGrpc
import cn.zjiali.server.grpc.api.group.GroupPluginConfigGrpc.GroupPluginConfigBlockingStub
import cn.zjiali.server.grpc.api.group.GroupPluginGrpc
import com.google.inject.Binder
import com.google.inject.Module
import com.google.inject.Provides
import com.google.inject.Singleton
import io.grpc.ManagedChannel

/**
 * @author zJiaLi
 * @since 2023-02-09 14:24
 */
class RpcServiceProvider : Module {
    override fun configure(binder: Binder) {}

    @Provides
    @Singleton
    fun configBlockingStub(
        managedChannel: ManagedChannel?
    ): ConfigBlockingStub {
        return ConfigGrpc.newBlockingStub(managedChannel)
    }

    @Provides
    @Singleton
    fun groupPluginConfigBlockingStub(managedChannel: ManagedChannel?): GroupPluginConfigBlockingStub {
        return GroupPluginConfigGrpc.newBlockingStub(managedChannel)
    }

    @Provides
    @Singleton
    fun groupPluginBlockingStub(managedChannel: ManagedChannel?): GroupPluginGrpc.GroupPluginBlockingStub {
        return GroupPluginGrpc.newBlockingStub(managedChannel)
    }
}