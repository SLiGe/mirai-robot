package cn.zjiali.robot.main.websocket;

import cn.zjiali.robot.factory.ServiceFactory;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author zJiaLi
 * @since 2021-07-29 10:19
 */
public class RobotWebSocketListener extends WebSocketListener {

    private final WebSocketManager webSocketManager = ServiceFactory.getInstance().getBean(WebSocketManager.class.getSimpleName(), WebSocketManager.class);

    public RobotWebSocketListener() {
        super();
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        webSocketManager.removeSession("");
        super.onClosed(webSocket, code, reason);

    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        webSocketManager.handleMessage(webSocket, text);
        super.onMessage(webSocket, text);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        webSocketManager.putSession(webSocket);
        super.onOpen(webSocket, response);
    }
}
