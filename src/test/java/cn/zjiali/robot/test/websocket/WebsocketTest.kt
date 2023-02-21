package cn.zjiali.robot.test.websocket

import cn.zjiali.robot.main.ApplicationBootStrap
import cn.zjiali.robot.main.websocket.WebSocketFactory
import cn.zjiali.robot.main.websocket.WebSocketManager
import cn.zjiali.robot.manager.WsSecurityManager
import cn.zjiali.robot.util.GuiceUtil
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * @author zJiaLi
 * @since 2022-02-18 14:51
 */
class WebsocketTest {

    @Before
    fun before(){
        System.setProperty("server.env", "dev")
        System.setProperty("application.config.file.local", "false")
        System.setProperty("application.config.file.local", "false")
    }
    @Test
    @Throws(InterruptedException::class)
    fun init() {
        try {
            ApplicationBootStrap.getInstance().init()
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: InstantiationException) {
            throw RuntimeException(e)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(e)
        }
        val defaultWsSecurityManager = GuiceUtil.getBean("DefaultWsSecurityManager",  WsSecurityManager::class.java)
        val webSocketManager = GuiceUtil.getBean(WebSocketManager::class.java)
        val webSocketFactory = WebSocketFactory()
        webSocketFactory.connect("wss://robot.zjiali.cn/websocket","357078415","b83689a385e5595d9a2a33e60a0bd8aa")
        val channel = webSocketFactory.channel()
        channel.writeAndFlush(TextWebSocketFrame(defaultWsSecurityManager.encryptMsgData("{\"robot\":\"2364051402\",\"msgType\":1,\"messageBody\":{\"qq\":357078415,\"nickName\":\"(:\uD83C\uDDE8\uD83C\uDDF3\",\"content\":\"干什么呢\"}}")))
        Thread.sleep(5000)
        channel.writeAndFlush(TextWebSocketFrame(defaultWsSecurityManager.encryptMsgData("{\"robot\":\"2364051402\",\"msgType\":1,\"messageBody\":{\"qq\":357078415,\"nickName\":\"(:\uD83C\uDDE8\uD83C\uDDF3\",\"content\":\"中午吃饭了吗\"}}")))
        Thread.sleep(5000)
        channel.writeAndFlush(TextWebSocketFrame(defaultWsSecurityManager.encryptMsgData("{\"robot\":\"2364051402\",\"msgType\":1,\"messageBody\":{\"qq\":357078415,\"nickName\":\"(:\uD83C\uDDE8\uD83C\uDDF3\",\"content\":\"吃什么了\"}}")))
        Thread.sleep(5000)

    }
}