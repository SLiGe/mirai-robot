package cn.zjiali.robot.main.websocket;

import cn.zjiali.robot.annotation.Autowired;
import cn.zjiali.robot.annotation.Component;
import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.manager.WsSecurityManager;
import cn.zjiali.robot.model.response.ws.WsResult;
import cn.zjiali.robot.service.WebSocketService;
import cn.zjiali.robot.util.CommonLogger;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.PropertiesUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.io.IOException;
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
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private WsSecurityManager wsSecurityManager;
    private final CommonLogger commonLogger = new CommonLogger(WebSocketManager.class.getSimpleName(), WebSocketManager.class);

    public void connect() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .pingInterval(20, TimeUnit.SECONDS)
                .build();
        String webSocketUrl = PropertiesUtil.getApplicationProperty("robot.websocket.url");
        Request request = new Request.Builder().url(webSocketUrl)
                .header("robotQQ", AppConfig.getQQ())
                .header("ws-token", wsSecurityManager.genWsToken(AppConfig.getQQ()))
                .build();

        okHttpClient.newWebSocket(request, new RobotWebSocketListener());

    }

    public void handleMessage(WebSocket webSocket, String text) {
        String decryptMsgData = wsSecurityManager.decryptMsgData(text);
        commonLogger.info("[WebSocket]====收到消息: {}", text);
        WsResult wsResult = JsonUtil.json2obj(decryptMsgData, WsResult.class);
        webSocketService.handleWsResult(wsResult);
    }

    public void putSession(WebSocket webSocket) {
        webSocketClientMap.put(AppConfig.getQQ(), WebSocketClient.builder().webSocket(webSocket).robotQQ(AppConfig.getQQ()).build());
    }

    public void removeSession() {
        webSocketClientMap.remove(AppConfig.getQQ());
    }

    public void removeSession(String robotQQ) {
        webSocketClientMap.remove(robotQQ);
    }

}
