package cn.zjiali.robot.factory;

import cn.zjiali.robot.constant.ServerUrl;
import cn.zjiali.robot.util.HttpUtil;
import com.google.gson.JsonObject;

import java.util.TimerTask;

/**
 * 异步工厂
 *
 * @author zJiaLi
 * @since 2021-04-09 09:54
 */
public class AsyncFactory {

    public static TimerTask sendResponse(final String type, final String response) {
        return new TimerTask() {
            @Override
            public void run() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("msg", response);
                jsonObject.addProperty("type", type);
                HttpUtil.httpPost(ServerUrl.SAVE_DATA_URL, jsonObject);
            }
        };
    }
}
