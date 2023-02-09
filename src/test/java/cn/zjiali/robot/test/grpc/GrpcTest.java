package cn.zjiali.robot.test.grpc;

import cn.zjiali.robot.guice.provider.RpcManagedChannelProvider;
import cn.zjiali.robot.guice.provider.RpcServiceProvider;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.util.ConfigService;
import cn.zjiali.robot.util.GuiceUtil;
import cn.zjiali.server.grpc.api.ConfigGrpc;
import cn.zjiali.server.grpc.api.GetConfigRequest;
import cn.zjiali.server.grpc.api.GetConfigResponse;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * @author zJiaLi
 * @since 2023-02-09 13:06
 */
public class GrpcTest {

    @Test
    public void testConfig(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 18090)
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
    public void testRobotEnv() throws IOException, InterruptedException {
        ApplicationBootStrap.getInstance().init();
        ConfigService configService = GuiceUtil.getBean(ConfigService.class);
        for (int i = 0; i < 10; i++) {
            String config = configService.getConfig("robot.reply.blacklist");
            System.out.println(config);
            Thread.sleep(30000);
        }
    }
}
