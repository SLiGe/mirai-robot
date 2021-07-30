package cn.zjiali.robot.service;

import cn.zjiali.robot.annotation.Service;
import cn.zjiali.robot.model.response.RobotBaseResponse;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.PropertiesUtil;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author zJiaLi
 * @since 2021-07-30 17:48
 */
@Service
public class DictService {

    public String getDictVal(Map<String, Object> paramMap) throws IOException {
        String dictUrl = PropertiesUtil.getApplicationProperty("robot.dict.query.url");
        String serverJson = HttpUtil.get(dictUrl, paramMap);
        Type type = new TypeToken<RobotBaseResponse<JsonObject>>() {
        }.getType();
        RobotBaseResponse<JsonObject> dictResponse = JsonUtil.toObjByType(serverJson, type);
        return dictResponse.getData().getAsJsonObject("data").get("val").getAsString();
    }
}
