package cn.zjiali.robot.service

import cn.zjiali.robot.annotation.Service
import cn.zjiali.robot.util.PluginConfigUtil.getConfigVal
import cn.zjiali.robot.constant.PluginCode
import cn.zjiali.robot.constant.PluginProperty
import cn.zjiali.robot.model.response.RobotBaseResponse
import cn.zjiali.robot.model.response.SignInDataResponse
import cn.zjiali.robot.util.HttpUtil
import cn.zjiali.robot.util.JsonUtil
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * 签到服务类
 *
 * @author zJiaLi
 * @since 2020-10-30 22:06
 */
@Service
class SignInService {
    fun doSignIn(qq: String?, group: String?, msgType: Int): SignInDataResponse? {
        val jsonObject = JsonObject()
        jsonObject.addProperty("qq", qq)
        jsonObject.addProperty("group", group)
        // 1好友消息 2群组消息
        jsonObject.addProperty("msgType", msgType)
        val point = Random(20).nextInt(60)
        jsonObject.addProperty("points", point)
        val signInDataJson = HttpUtil.post(getConfigVal(PluginCode.SIGN, PluginProperty.SIGN_URL), jsonObject)
        val type = object : TypeToken<RobotBaseResponse<SignInDataResponse?>?>() {}.type
        val robotBaseResponse = JsonUtil.toObjByType<RobotBaseResponse<SignInDataResponse>>(signInDataJson, type)
        if (robotBaseResponse.status == 500) {
            return null
        }
        val signInDataResponse = robotBaseResponse.data
        if (signInDataResponse.status == 203) {
            return signInDataResponse
        }
        signInDataResponse.dataResponse?.getPoints = point
        return signInDataResponse
    }

    fun getSignInData(qq: String?, group: String?, msgType: Int): SignInDataResponse? {
        val jsonObject = JsonObject()
        jsonObject.addProperty("qq", qq)
        jsonObject.addProperty("group", group)
        // 1好友消息 2群组消息
        jsonObject.addProperty("msgType", msgType)
        val signInDataJson = HttpUtil.post(getConfigVal(PluginCode.SIGN, PluginProperty.QUERY_SIGN_URL), jsonObject)
        val type = object : TypeToken<RobotBaseResponse<SignInDataResponse?>?>() {}.type
        val robotBaseResponse = JsonUtil.toObjByType<RobotBaseResponse<SignInDataResponse>>(signInDataJson, type)
        return if (robotBaseResponse.status == 200) {
            robotBaseResponse.data
        } else null
    }
}