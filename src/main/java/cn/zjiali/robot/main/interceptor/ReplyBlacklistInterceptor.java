package cn.zjiali.robot.main.interceptor;

import cn.zjiali.robot.annotation.Service;
import cn.zjiali.robot.entity.response.RobotBaseResponse;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.PropertiesUtil;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.utils.MiraiLogger;
import net.mamoe.mirai.utils.PlatformLogger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 回复黑名单拦截器
 *
 * @author zJiaLi
 * @since 2021-06-14 12:15
 */
@Service
public class ReplyBlacklistInterceptor implements HandlerInterceptor {

    private static final MiraiLogger miraiLogger = new PlatformLogger(ReplyBlacklistInterceptor.class.getName());

    private String replyBlacklist;
    private String queryReplyBlacklistUrl;
    private final Map<String, Object> queryReplyBlacklistParamMap = Maps.newHashMap();

    public ReplyBlacklistInterceptor() {
        this.queryReplyBlacklistParamMap.put("dictCode", "QrReplyBlacklist");
        this.queryReplyBlacklistParamMap.put("dictTypeCode", "D00001");
        try {
            this.replyBlacklist = PropertiesUtil.getProperty("application.properties", "robot.reply.blacklist");
            this.queryReplyBlacklistUrl = PropertiesUtil.getProperty("application.properties", "robot.reply.blacklist.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean preHandle(MessageEvent messageEvent) throws Exception {
        String serverReplyBlacklistJson = HttpUtil.get(this.queryReplyBlacklistUrl, this.queryReplyBlacklistParamMap);
        Type type = new TypeToken<RobotBaseResponse<JsonObject>>() {
        }.getType();
        RobotBaseResponse<JsonObject> serverReplyBlacklist = JsonUtil.toObjByType(serverReplyBlacklistJson, type);
        String serverReplyBlacklistStr = serverReplyBlacklist.getData().getAsJsonObject("data").get("val").getAsString();
        String qq = Long.toString(messageEvent.getSender().getId());
        if (serverReplyBlacklistStr.contains(qq) || this.replyBlacklist.contains(qq)) {
            miraiLogger.info("QQ: " + qq + " in reply blacklist!");
            return false;
        }
        return HandlerInterceptor.super.preHandle(messageEvent);
    }


}
