package cn.zjiali.robot.util;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.json.JSONObject;
import net.mamoe.mirai.utils.DeviceInfo;

import java.io.File;

/**
 * 设备信息工具类
 *
 * @author zJiaLi
 * @since 2021-04-17 14:46
 */
public class DeviceUtil {
    private DeviceUtil() {
    }

    /**
     * 获取机器人设备信息的JSON字符串
     *
     * @return
     */
    public static String getDeviceInfoJson(String qq) {
        // 设备信息文件
        File file = new File("deviceInfo-".concat(qq).concat(".json"));
        String deviceInfoJson = null;
        if (file.exists()) {
            FileReader fileReader = new FileReader(file);
            deviceInfoJson = fileReader.readString();
        } else {
            deviceInfoJson = new JSONObject(DeviceInfo.random()).toString();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(deviceInfoJson);
        }
        return deviceInfoJson;
    }
}
