package cn.zjiali.robot.service

import cn.zjiali.robot.model.response.RobotBaseResponse
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import cn.zjiali.robot.util.PropertiesUtil
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.google.inject.Singleton
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author zJiaLi
 * @since 2021-07-30 17:48
 */
@Singleton
class DictService {

    private var cache: Cache<String, String> = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build()

    @Throws(IOException::class)
    fun getDictVal(cacheKey: String, paramMap: MutableMap<String, Any>): String {
        return cache.get(cacheKey) { getDictVal(paramMap) }
    }

    @Throws(IOException::class)
    fun getDictVal(paramMap: MutableMap<String, Any>): String {
        val dictUrl = PropertiesUtil.getApplicationProperty("robot.dict.query.url")
        val serverJson = HttpUtil.get(dictUrl, paramMap)
        val type = object : TypeToken<RobotBaseResponse<JsonObject?>?>() {}.type
        val dictResponse = JsonUtil.toObjByType<RobotBaseResponse<JsonObject>>(serverJson, type)
        return dictResponse.data?.get("val")!!.asString
    }
}