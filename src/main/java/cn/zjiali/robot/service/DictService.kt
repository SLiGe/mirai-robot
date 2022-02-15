package cn.zjiali.robot.service

import cn.zjiali.robot.annotation.Service
import kotlin.Throws
import java.io.IOException
import cn.zjiali.robot.model.response.RobotBaseResponse
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import cn.zjiali.robot.util.PropertiesUtil
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

/**
 * @author zJiaLi
 * @since 2021-07-30 17:48
 */
@Service
class DictService {

    @Throws(IOException::class)
    fun getDictVal(paramMap: MutableMap<String, Any>): String {
        val dictUrl = PropertiesUtil.getApplicationProperty("robot.dict.query.url")
        val serverJson = HttpUtil.get(dictUrl, paramMap)
        val type = object : TypeToken<RobotBaseResponse<JsonObject?>?>() {}.type
        val dictResponse = JsonUtil.toObjByType<RobotBaseResponse<JsonObject>>(serverJson, type)
        return dictResponse.data["val"].asString
    }
}