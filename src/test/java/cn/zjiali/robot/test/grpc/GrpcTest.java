package cn.zjiali.robot.test.grpc;

import cn.zjiali.robot.guice.provider.RpcManagedChannelProvider;
import cn.zjiali.robot.guice.provider.RpcServiceProvider;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.main.interceptor.HandlerInterceptor;
import cn.zjiali.robot.main.interceptor.ReplyBlacklistHandlerInterceptor;
import cn.zjiali.robot.manager.RobotManager;
import cn.zjiali.robot.service.ConfigService;
import cn.zjiali.robot.util.GuiceUtil;
import cn.zjiali.server.grpc.api.*;
import cn.zjiali.server.grpc.api.group.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author zJiaLi
 * @since 2023-02-09 13:06
 */
public class GrpcTest {

    @Test
    public void testConfig() {

        ManagedChannel channel = NettyChannelBuilder.forTarget("127.0.0.1:18090")
                .usePlaintext()
                .build();
        List<ConfigResponse> configListList = GroupPluginConfigGrpc.newBlockingStub(channel)
                .queryConfig(QueryConfigRequest.newBuilder().build()).getConfigListList();

        GetConfigResponse configResponse = ConfigGrpc.newBlockingStub(channel).getConfig(GetConfigRequest.newBuilder()
                .setConfigKey("robot.reply.blacklist").build());
        System.out.println(configResponse);
    }

    @Test
    public void testGuiceGrpc() {
        Injector injector = Guice.createInjector(new RpcManagedChannelProvider(), new RpcServiceProvider());
        ConfigGrpc.ConfigBlockingStub instance = injector.getInstance(ConfigGrpc.ConfigBlockingStub.class);
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("server-token", Metadata.ASCII_STRING_MARSHALLER), "sss");
        ConfigGrpc.ConfigBlockingStub configBlockingStub = instance.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));
        GetConfigResponse config = configBlockingStub.getConfig(GetConfigRequest.newBuilder()
                .setConfigKey("robot.reply.blacklist").build());
        System.out.println(config);
    }

    @Test
    public void testGroupPluginConfig() throws IOException {
        System.setProperty("server.env", "dev");
        System.setProperty("application.config.file.local", "false");
        System.setProperty("application.config.file.local","false");
        ApplicationBootStrap.getInstance().init();
        ManagedChannel channel = GuiceUtil.getBean(ManagedChannel.class);
        List<ConfigResponse> configListList = GroupPluginConfigGrpc.newBlockingStub(channel)
                .queryConfig(QueryConfigRequest.newBuilder().build()).getConfigListList();
        GroupPluginGrpc.GroupPluginBlockingStub groupPluginBlockingStub = GuiceUtil.getBean(GroupPluginGrpc.GroupPluginBlockingStub.class);
        GetPluginResponse pluginResponse = groupPluginBlockingStub.getPlugin(GetPluginRequest.newBuilder().setGroupNumber(271613003L).build());
        System.out.println(1);
    }


    @Test
    public void testRobotEnv() throws Exception {
        List<HandlerInterceptor> handlerInterceptors = GuiceUtil.getMultiBean(HandlerInterceptor.class);
        HandlerInterceptor handlerInterceptor = handlerInterceptors.stream().filter(p -> p instanceof ReplyBlacklistHandlerInterceptor).findFirst().get();
        RobotManager robotManager = GuiceUtil.getBean(RobotManager.class);
        handlerInterceptor.preHandle(null);
        ConfigService configService = GuiceUtil.getBean(ConfigService.class);
        for (int i = 0; i < 10; i++) {
            String config = configService.getConfig("robot.reply.blacklist");
            System.out.println(config);

            Thread.sleep(30000);
        }
    }
}
