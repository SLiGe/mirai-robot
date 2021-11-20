package cn.zjiali.robot.main.websocket;

import lombok.Builder;
import lombok.Data;
import okhttp3.WebSocket;

/**
 * @author zJiaLi
 * @since 2021-07-29 10:16
 */
@Data
@Builder
public class WebSocketClient {

    private String robotQQ;

    private WebSocket webSocket;

}
