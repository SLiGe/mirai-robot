package cn.zjiali.robot.guice.module;

import cn.zjiali.robot.handler.DefaultGlobalMessageHandler;
import cn.zjiali.robot.handler.GlobalMessageHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * @author zJiaLi
 * @since 2022-05-28 23:18
 */
public class SimpleMessageEventHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GlobalMessageHandler.class).annotatedWith(Names.named(DefaultGlobalMessageHandler.class.getSimpleName())).to(DefaultGlobalMessageHandler.class).in(Singleton.class);;
    }
}
