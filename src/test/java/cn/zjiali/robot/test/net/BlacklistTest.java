package cn.zjiali.robot.test.net;

import cn.zjiali.robot.model.response.RobotBaseResponse;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.PropertiesUtil;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author zJiaLi
 * @since 2021-06-15 17:31
 */
public class BlacklistTest {

    @Test
    public void testBlacklist() throws IOException {
        Map<String, Object> queryReplyBlacklistParamMap = Maps.newHashMap();
        queryReplyBlacklistParamMap.put("dictCode", "QrReplyBlacklist");
        queryReplyBlacklistParamMap.put("dictTypeCode", "D00001");
        String replyBlacklist = PropertiesUtil.getProperty("application.properties", "robot.reply.blacklist");
        String queryReplyBlacklistUrl = PropertiesUtil.getProperty("application.properties", "robot.reply.blacklist.url");
        String serverReplyBlacklistJson = HttpUtil.get(queryReplyBlacklistUrl, queryReplyBlacklistParamMap);
        Type type = new TypeToken<RobotBaseResponse<JsonObject>>() {
        }.getType();
        RobotBaseResponse<JsonObject> serverReplyBlacklist = JsonUtil.toObjByType(serverReplyBlacklistJson, type);
        String serverReplyBlacklistStr = serverReplyBlacklist.getData().get("val").getAsString();
        System.out.println("local replyBlacklist: " + replyBlacklist);
        System.out.println("serverReplyBlacklistStr: " + serverReplyBlacklistStr);
    }
}
