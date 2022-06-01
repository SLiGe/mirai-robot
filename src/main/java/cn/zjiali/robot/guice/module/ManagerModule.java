package cn.zjiali.robot.guice.module;

import cn.zjiali.robot.manager.DefaultWsSecurityManager;
import cn.zjiali.robot.manager.WsSecurityManager;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * @author zJiaLi
 * @since 2022-05-28 23:54
 */
public class ManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(WsSecurityManager.class).annotatedWith(Names.named("DefaultWsSecurityManager")).to(DefaultWsSecurityManager.class).in(Singleton.class);
    }
}
