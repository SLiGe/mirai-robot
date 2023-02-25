package cn.zjiali.robot.guice.module;

import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.handler.*;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * @author zJiaLi
 * @since 2022-05-28 23:18
 */
public class GlobalEventHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
        if (AppConfig.applicationConfig.getServerControl() == 1) {
            bind(GlobalEventHandler.class).to(ServerGlobalEventHandler.class).in(Singleton.class);
        } else {
            bind(GlobalEventHandler.class).to(DefaultGlobalEventHandler.class).in(Singleton.class);
        }
    }
}
