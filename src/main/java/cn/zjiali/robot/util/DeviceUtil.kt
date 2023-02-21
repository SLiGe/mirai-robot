package cn.zjiali.robot.util

import cn.hutool.core.io.file.FileReader
import cn.hutool.core.io.file.FileWriter
import cn.zjiali.robot.constant.Constants
import cn.zjiali.robot.model.response.JokeResponse
import kotlinx.serialization.encodeToString
import net.mamoe.mirai.utils.DeviceInfo
import java.io.File

/**
 * 设备信息工具类
 *
 * @author zJiaLi
 * @since 2021-04-17 14:46
 */
object DeviceUtil {
    /**
     * 获取机器人设备信息的JSON字符串
     *
     * @return
     */
    @JvmStatic
    fun getDeviceInfoJson(qq: String): String? {
        // 设备信息文件
        val file = File(System.getProperty("robot.workdir") + "/deviceInfo-$qq.json")
        val deviceInfoJson: String?
        if (file.exists()) {
            val fileReader = FileReader(file)
            deviceInfoJson = fileReader.readString()
        } else {
            deviceInfoJson = Constants.JSON.encodeToString(DeviceInfo.random())
            val fileWriter = FileWriter(file)
            fileWriter.write(deviceInfoJson)
        }
        return deviceInfoJson
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val res = JokeResponse()
        res.content = "aaa"
//        println(JSONObject(MiraiDeviceGenerator().generate()).toString())
    }
}