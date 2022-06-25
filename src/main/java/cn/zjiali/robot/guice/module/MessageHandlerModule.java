package cn.zjiali.robot.guice.module;

import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.config.Plugin;
import cn.zjiali.robot.handler.*;
import cn.zjiali.robot.util.CommonLogger;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * @author zJiaLi
 * @since 2022-05-28 23:54
 */
@Slf4j
public class MessageHandlerModule extends AbstractModule {

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {
        Multibinder<MessageEventHandler> messageEventHandlerMultiBinder = Multibinder.newSetBinder(binder(), MessageEventHandler.class);
        List<Plugin> plugins = AppConfig.getApplicationConfig().getPlugins();
        Optional.ofNullable(plugins).ifPresent(list -> list.forEach(plugin -> {
            if (plugin.getEnable() == 1) {
                try {
                    messageEventHandlerMultiBinder.addBinding()
                            .to((Class<? extends MessageEventHandler>) Class.forName(plugin.getHandler()))
                            .in(Singleton.class);;
                    log.info("[loadAppConfig]====加载 [{}] 成功！", plugin.getName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
    }
}
