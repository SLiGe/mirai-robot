package cn.zjiali.robot.main.interceptor;

import cn.zjiali.robot.annotation.Service;
import cn.zjiali.robot.util.PropertiesUtil;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * 回复黑名单拦截器
 *
 * @author zJiaLi
 * @since 2021-06-14 12:15
 */
@Service
public class ReplyBlacklistInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(MessageEvent messageEvent) throws Exception {
        String replyBlacklist = PropertiesUtil.getProperty("application.properties","robot.reply.blacklist");
        if (replyBlacklist.contains(Long.toString(messageEvent.getSender().getId()))) {
            return false;
        }
        return HandlerInterceptor.super.preHandle(messageEvent);
    }


}
