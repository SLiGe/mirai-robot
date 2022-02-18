package cn.zjiali.robot.main.websocket

import okhttp3.WebSocket

/**
 * @author zJiaLi
 * @since 2021-07-29 10:16
 */
class WebSocketClient {
     var robotQQ: String? = null
     var webSocket: WebSocket? = null

    constructor(qq: String, webSocket: WebSocket?) {
        this.robotQQ = qq
        this.webSocket = webSocket
    }
}