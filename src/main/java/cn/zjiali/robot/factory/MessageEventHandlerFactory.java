package cn.zjiali.robot.factory;

import cn.zjiali.robot.handler.MessageEventHandler;
import cn.zjiali.robot.util.CommonLogger;

import java.lang.reflect.InvocationTargetException;

/**
 * @author zJiaLi
 * @since 2020-10-30 09:24
 */
@Deprecated
public class MessageEventHandlerFactory extends AbstractBeanFactory {

    private static final MessageEventHandlerFactory MESSAGE_EVENT_HANDLER_FACTORY = new MessageEventHandlerFactory();

    private final CommonLogger commonLogger = new CommonLogger(MessageEventHandlerFactory.class);

    public static MessageEventHandlerFactory getInstance() {
        return MESSAGE_EVENT_HANDLER_FACTORY;
    }

    public MessageEventHandler put(String pluginName, String handler) {
        try {
            commonLogger.info("加载消息处理器 === {}", handler);
            Class<?> handlerClass = Class.forName(handler);
            MessageEventHandler instance = (MessageEventHandler) handlerClass.getConstructor().newInstance();
            putBean(pluginName, instance);
            return instance;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MessageEventHandler get(String pluginName) {
        return getBean(pluginName, MessageEventHandler.class);
    }

    @Override
    public String beanPrefix() {
        return "MessageEventHandler-";
    }

}
