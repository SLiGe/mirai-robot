package cn.zjiali.robot.test.net;

import cn.zjiali.robot.factory.MessageFactory;
import cn.zjiali.robot.factory.ServiceFactory;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.main.interceptor.ReplyBlacklistHandlerInterceptor;
import cn.zjiali.robot.model.response.SignInDataResponse;
import cn.zjiali.robot.service.MoLiService;
import cn.zjiali.robot.service.SignInService;
import cn.zjiali.robot.util.PropertiesUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import org.junit.Test;

import java.io.IOException;

/**
 * @author zJiaLi
 * @since 2021-05-10 16:34
 */
public class SignInTest {


    @Test
    public void testSignIn() throws IOException {
        ApplicationBootStrap.getInstance().init();
        //处理器排序

        SignInService signInService = ServiceFactory.getInstance().get(SignInService.class.getSimpleName(), SignInService.class);
        MoLiService moLiService = ServiceFactory.getInstance().get(MoLiService.class.getSimpleName(), MoLiService.class);
        String commonChatMessage = moLiService.getCommonChatMessage(357078415,35078415,"你好呀");
        String jokeMessage = moLiService.getJokeMessage(357078415L, false, 123456L);

        ReplyBlacklistHandlerInterceptor replyBlacklistHandlerInterceptor = ServiceFactory.getInstance().get(ReplyBlacklistHandlerInterceptor.class.getSimpleName(), ReplyBlacklistHandlerInterceptor.class);
        String replyBlacklist = PropertiesUtil.getProperty("application.properties","robot.reply.blacklist");
        System.out.println(replyBlacklist.contains(Long.toString(357078415L)));

        System.out.println("jokeMessage: " + jokeMessage);
        System.out.println("commonChatMessage: " + commonChatMessage);
        final FriendMessageEvent friendMessageEvent = new FriendMessageEvent(null, null, 1);
        SignInDataResponse signInDataResponse = signInService.doSignIn("357078415", "123456", 1);
        SignInDataResponse signInData = signInService.getSignInData("357078415", "123456", 1);
        String fortuneMsg = MessageFactory.getFortuneMsg(357078415L, 123456L, 1);
        System.out.println(fortuneMsg);
    }
}
