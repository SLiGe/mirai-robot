package cn.zjiali.robot.manager

import cn.hutool.core.util.StrUtil
import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.model.server.AjaxResult
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import com.google.inject.Singleton
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 *
 * @author zJiaLi
 * @since 2022-06-24 11:27
 */
@Singleton
class ServerTokenManager {

    private var tokenReference: AtomicReference<String> = AtomicReference()

    private val reentrantLock: ReentrantLock = ReentrantLock(true)

    fun genServerToken() {
        reentrantLock.withLock {
            val serverUser = AppConfig.getApplicationConfig().serverUser
            val serverPwd = AppConfig.getApplicationConfig().serverPwd
            val loginBody = LoginBody(serverUser!!, serverPwd!!)
            val loginJson = HttpUtil.post(AppConfig.getApplicationConfig().serverUrl, JsonUtil.obj2str(loginBody))
            val ajaxResult = JsonUtil.json2obj(loginJson, AjaxResult::class.java)
            if (ajaxResult.success()) {
                if (StrUtil.isNotBlank(ajaxResult.token)) {
                    tokenReference.getAndSet(ajaxResult.token)
                }
            }
        }
    }

    fun serverToken(): String {
        reentrantLock.withLock { return this.tokenReference.get() }
    }

    data class LoginBody(val username: String, val password: String)
}