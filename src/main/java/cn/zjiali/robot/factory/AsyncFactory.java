package cn.zjiali.robot.factory;

import cn.zjiali.robot.constant.ServerUrl;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.PropertiesUtil;
import com.google.gson.JsonObject;

import java.util.TimerTask;

/**
 * 异步工厂
 *
 * @author zJiaLi
 * @since 2021-04-09 09:54
 */
public class AsyncFactory {

    public static String SAVE_DATA_URL = "saveData.api";

    public static TimerTask sendResponse(final String type, final String response) {
        return new TimerTask() {
            @Override
            public void run() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("response", response);
                jsonObject.addProperty("type", type);
                HttpUtil.post(PropertiesUtil.getApiProperty(SAVE_DATA_URL), jsonObject);
            }
        };
    }
}
