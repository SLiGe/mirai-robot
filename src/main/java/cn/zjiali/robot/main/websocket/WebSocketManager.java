package cn.zjiali.robot.main.websocket;

import cn.zjiali.robot.annotation.Component;
import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.entity.ApplicationConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zJiaLi
 * @since 2021-07-29 10:18
 */
@Component
public class WebSocketManager {

    private final Map<String, WebSocketClient> webSocketClientMap = new ConcurrentHashMap<>();

    public void connect() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .pingInterval(20, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url("").build();

        WebSocket webSocket = okHttpClient.newWebSocket(request, new RobotWebSocketListener());

    }

    public void putSession(WebSocket webSocket) {
        ApplicationConfig applicationConfig = AppConfig.getApplicationConfig();
        webSocketClientMap.put(applicationConfig.getQq(), WebSocketClient.builder().webSocket(webSocket).robotQQ(applicationConfig.getQq()).build());
    }

    public void removeSession(String robotQQ) {
        webSocketClientMap.remove(robotQQ);
    }

}
