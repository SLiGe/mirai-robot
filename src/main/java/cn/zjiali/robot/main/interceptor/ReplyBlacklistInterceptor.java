package cn.zjiali.robot.main.interceptor;

import cn.zjiali.robot.annotation.Autowired;
import cn.zjiali.robot.annotation.Service;
import cn.zjiali.robot.service.DictService;
import cn.zjiali.robot.util.PropertiesUtil;
import com.google.common.collect.Maps;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.utils.MiraiLogger;
import net.mamoe.mirai.utils.PlatformLogger;

import java.io.IOException;
import java.util.Map;

/**
 * 回复黑名单拦截器
 *
 * @author zJiaLi
 * @since 2021-06-14 12:15
 */
@Service
public class ReplyBlacklistInterceptor implements HandlerInterceptor {

    private static final MiraiLogger miraiLogger = new PlatformLogger(ReplyBlacklistInterceptor.class.getSimpleName());
    @Autowired
    private DictService dictService;

    private String replyBlacklist;
    private final Map<String, Object> queryReplyBlacklistParamMap = Maps.newHashMap();

    public ReplyBlacklistInterceptor() {
        this.queryReplyBlacklistParamMap.put("dictCode", "QrReplyBlacklist");
        this.queryReplyBlacklistParamMap.put("dictTypeCode", "D00001");
        try {
            this.replyBlacklist = PropertiesUtil.getApplicationProperty("robot.reply.blacklist");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean preHandle(MessageEvent messageEvent) throws Exception {
        String serverReplyBlacklistStr = dictService.getDictVal(queryReplyBlacklistParamMap);
        String qq = Long.toString(messageEvent.getSender().getId());
        if (serverReplyBlacklistStr.contains(qq) || this.replyBlacklist.contains(qq)) {
            miraiLogger.info("QQ: " + qq + " in reply blacklist!");
            return false;
        }
        return HandlerInterceptor.super.preHandle(messageEvent);
    }


}
