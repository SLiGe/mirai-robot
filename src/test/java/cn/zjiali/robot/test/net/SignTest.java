package cn.zjiali.robot.test.net;

import cn.zjiali.robot.factory.DefaultBeanFactory;
import cn.zjiali.robot.factory.ServiceFactory;
import cn.zjiali.robot.handler.SpiritSignMessageEventHandler;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.main.OutMessageConvert;
import cn.zjiali.robot.main.interceptor.HandlerInterceptor;
import cn.zjiali.robot.manager.WsSecurityManager;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.model.response.ws.WsClientRes;
import cn.zjiali.robot.util.GuiceUtil;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zJiaLi
 * @since 2022-01-31 23:54
 */
public class SignTest {

    @Test
    public void testSign() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        ApplicationBootStrap.getInstance().init();
        Injector injector = ApplicationBootStrap.getInstance().getInjector();
        List<Binding<HandlerInterceptor>> bindingsByType = injector.findBindingsByType(TypeLiteral.get(HandlerInterceptor.class));
        Map<Key<?>, Binding<?>> bindings = injector.getBindings();
        for (Binding<HandlerInterceptor> handlerInterceptorBinding : bindingsByType) {
            HandlerInterceptor handlerInterceptor = handlerInterceptorBinding.getProvider().get();
            System.out.println(handlerInterceptor.getClass().getName());
        }
        Set<Map.Entry<Key<?>, Binding<?>>> entries = bindings.entrySet();
        for (Map.Entry<Key<?>, Binding<?>> entry : entries) {
            Key<?> key = entry.getKey();
            Binding<?> value = entry.getValue();

        }
        System.out.println(1);
        WsSecurityManager defaultWsSecurityManager = GuiceUtil.getBean("DefaultWsSecurityManager", WsSecurityManager.class);
        defaultWsSecurityManager.encryptMsgData(new WsClientRes(200, "处理成功!").toJson());
        SpiritSignMessageEventHandler spiritSignMessageEventHandler = new SpiritSignMessageEventHandler();
        OutMessage gyMessage = spiritSignMessageEventHandler.signPerDay("观音灵签", "357078415");
        String gy = OutMessageConvert.getInstance().convert(gyMessage);
        System.out.println(gy);
        System.out.println();
        OutMessage ylMessage = spiritSignMessageEventHandler.signPerDay("月老灵签", "357078415");
        String yl = OutMessageConvert.getInstance().convert(ylMessage);
        System.out.println(yl);
        System.out.println();
        OutMessage csMessage = spiritSignMessageEventHandler.signPerDay("财神灵签", "357078415");
        String cs = OutMessageConvert.getInstance().convert(csMessage);
        System.out.println(cs);
    }
}
