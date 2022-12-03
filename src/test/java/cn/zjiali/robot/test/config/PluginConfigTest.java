package cn.zjiali.robot.test.config;

import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.handler.ServerGlobalMessageHandler;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.main.interceptor.ReplyBlacklistHandlerInterceptor;
import cn.zjiali.robot.manager.PluginManager;
import cn.zjiali.robot.util.GuiceUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * @author zJiaLi
 * @since 2022-07-01 23:49
 */
public class PluginConfigTest {


    @Test
    public void testGetPluginConfig() throws IOException {
        ApplicationBootStrap.getInstance().init();
        PluginManager pluginManager = GuiceUtil.getBean(PluginManager.class);


        pluginManager.injectClassField(new ServerGlobalMessageHandler());
        String command = pluginManager.getConfigVal(PluginCode.ONE_SEN, "command", 859744199L, 357078415L);
        pluginManager.refreshPlugin();
        System.out.println(command);
    }
}
