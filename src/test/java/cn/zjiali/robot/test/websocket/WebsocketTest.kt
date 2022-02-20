package cn.zjiali.robot.test.websocket;

import cn.zjiali.robot.factory.ServiceFactory;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.main.websocket.WebSocketManager;
import cn.zjiali.robot.manager.WsSecurityManager;
import okhttp3.WebSocket;
import org.junit.Test;

import java.io.IOException;

/**
 * @author zJiaLi
 * @since 2022-02-18 14:51
 */
public class WebsocketTest {

    @Test
    public void init() throws InterruptedException {
        try {
            ApplicationBootStrap.getInstance().init();
        } catch (IOException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        WsSecurityManager defaultWsSecurityManager = ServiceFactory.getInstance().get("DefaultWsSecurityManager", WsSecurityManager.class);
        WebSocketManager webSocketManager = ServiceFactory.getInstance().getBean(WebSocketManager.class.getSimpleName(), WebSocketManager.class);
        WebSocket webSocket = webSocketManager.getSession("xxx");
        for (int i = 0; i < 3; i++) {
            webSocket.send(defaultWsSecurityManager.encryptMsgData("{\"msg\":\"ok\",\"code\":200}"));
        }
        Thread.sleep(100000);

    }
}
