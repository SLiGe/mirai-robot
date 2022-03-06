package cn.zjiali.robot.test.websocket

import cn.zjiali.robot.factory.ServiceFactory
import cn.zjiali.robot.main.ApplicationBootStrap
import cn.zjiali.robot.main.websocket.WebSocketManager
import cn.zjiali.robot.manager.WsSecurityManager
import com.google.common.base.Function
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import org.junit.Test
import java.io.IOException

/**
 * @author zJiaLi
 * @since 2022-02-18 14:51
 */
class WebsocketTest {
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
        val defaultWsSecurityManager =
            ServiceFactory.getInstance().get("DefaultWsSecurityManager", WsSecurityManager::class.java)
        val webSocketManager = ServiceFactory.getInstance().getBean(
            WebSocketManager::class.java.simpleName, WebSocketManager::class.java
        )
        val webSocket = webSocketManager.getSession("xxx")
        for (i in 0..2) {
            webSocket!!.send(defaultWsSecurityManager.encryptMsgData("{\"msg\":\"ok\",\"code\":200}"))
        }
        Thread.sleep(10100000)

    }
}