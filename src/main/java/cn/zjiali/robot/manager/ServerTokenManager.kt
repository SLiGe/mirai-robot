package cn.zjiali.robot.manager

import cn.hutool.core.util.StrUtil
import cn.zjiali.robot.config.AppConfig
import cn.zjiali.robot.model.server.AjaxResult
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 *
 * @author zJiaLi
 * @since 2022-06-24 11:27
 */
class ServerTokenManager {

    private val tokenReference: AtomicReference<String> = AtomicReference()

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
                    tokenReference.set(ajaxResult.token)
                }
            }
        }
    }

    fun serverToken(): String {
        reentrantLock.withLock {
            return tokenReference.get()
        }
    }

    data class LoginBody(val username: String, val password: String)
}