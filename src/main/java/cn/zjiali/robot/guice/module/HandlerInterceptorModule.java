package cn.zjiali.robot.guice.module;

import cn.zjiali.robot.main.interceptor.HandlerInterceptor;
import cn.zjiali.robot.main.interceptor.PushMessageHandlerInterceptor;
import cn.zjiali.robot.main.interceptor.ReplyBlacklistHandlerInterceptor;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

/**
 * @author zJiaLi
 * @since 2022-05-28 23:18
 */
public class HandlerInterceptorModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<HandlerInterceptor> multiBinder = Multibinder.newSetBinder(binder(), HandlerInterceptor.class);
        multiBinder.addBinding().to(ReplyBlacklistHandlerInterceptor.class).in(Singleton.class);
        multiBinder.addBinding().to(PushMessageHandlerInterceptor.class).in(Singleton.class);
    }
}
