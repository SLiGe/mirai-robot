package cn.zjiali.robot.guice.module;

import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.handler.DefaultGlobalMessageHandler;
import cn.zjiali.robot.handler.GlobalMessageHandler;
import cn.zjiali.robot.handler.ServerGlobalMessageHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * @author zJiaLi
 * @since 2022-05-28 23:18
 */
public class SimpleMessageEventHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
        if (AppConfig.applicationConfig.getServerControl() == 1) {
            bind(GlobalMessageHandler.class).to(ServerGlobalMessageHandler.class).in(Singleton.class);
        } else {
            bind(GlobalMessageHandler.class).to(DefaultGlobalMessageHandler.class).in(Singleton.class);
        }

    }
}
