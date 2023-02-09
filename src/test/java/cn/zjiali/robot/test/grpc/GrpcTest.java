package cn.zjiali.robot.test.grpc;

import cn.zjiali.robot.guice.provider.RpcManagedChannelProvider;
import cn.zjiali.robot.guice.provider.RpcServiceProvider;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.main.interceptor.HandlerInterceptor;
import cn.zjiali.robot.main.interceptor.ReplyBlacklistHandlerInterceptor;
import cn.zjiali.robot.manager.RobotManager;
import cn.zjiali.robot.service.ConfigService;
import cn.zjiali.robot.util.GuiceUtil;
import cn.zjiali.server.grpc.api.ConfigGrpc;
import cn.zjiali.server.grpc.api.GetConfigRequest;
import cn.zjiali.server.grpc.api.GetConfigResponse;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.internal.contact.FriendImpl;
import net.mamoe.mirai.internal.contact.info.FriendInfoImpl;
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
    public void testConfig(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("192.168.0.100", 18090)
                .usePlaintext()
                .build();
        GetConfigResponse configResponse = ConfigGrpc.newBlockingStub(channel).getConfig(GetConfigRequest.newBuilder()
                .setConfigKey("robot.reply.blacklist").build());
        System.out.println(configResponse);
    }

    @Test
    public void testGuiceGrpc(){
        Injector injector = Guice.createInjector(new RpcManagedChannelProvider(),new RpcServiceProvider());
        ConfigGrpc.ConfigBlockingStub instance = injector.getInstance(ConfigGrpc.ConfigBlockingStub.class);
        GetConfigResponse config = instance.getConfig(GetConfigRequest.newBuilder()
                .setConfigKey("robot.reply.blacklist").build());
        System.out.println(config);
    }

    @Test
    public void testRobotEnv() throws Exception {
        ApplicationBootStrap.getInstance().init();
        List<HandlerInterceptor> handlerInterceptors = GuiceUtil.getMultiBean(HandlerInterceptor.class);
        HandlerInterceptor handlerInterceptor = handlerInterceptors.stream().filter(p -> p instanceof ReplyBlacklistHandlerInterceptor).findFirst().get();
        RobotManager robotManager = GuiceUtil.getBean(RobotManager.class);
        handlerInterceptor.preHandle(null );
        ConfigService configService = GuiceUtil.getBean(ConfigService.class);
        for (int i = 0; i < 10; i++) {
            String config = configService.getConfig("robot.reply.blacklist");
            System.out.println(config);

            Thread.sleep(30000);
        }
    }
}
