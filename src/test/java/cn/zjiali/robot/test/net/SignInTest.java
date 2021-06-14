package cn.zjiali.robot.test.net;

import cn.zjiali.robot.entity.response.SignInDataResponse;
import cn.zjiali.robot.factory.MessageFactory;
import cn.zjiali.robot.factory.ServiceFactory;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.main.interceptor.ReplyBlacklistInterceptor;
import cn.zjiali.robot.service.MoLiService;
import cn.zjiali.robot.service.SignInService;
import cn.zjiali.robot.util.PropertiesUtil;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.IOException;

/**
 * @author zJiaLi
 * @since 2021-05-10 16:34
 */
public class SignInTest {


    @Test
    public void testSignIn() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        ApplicationBootStrap.getInstance().init();
        SignInService signInService = ServiceFactory.getInstance().get(SignInService.class.getSimpleName(), SignInService.class);
        MoLiService moLiService = ServiceFactory.getInstance().get(MoLiService.class.getSimpleName(), MoLiService.class);
        String yllqMessage = moLiService.getYllqMessage(357078415L, false, 123456L);
        String csylqMessage = moLiService.getCsylqMessage(357078415L, false, 123456L);
        String gylqMessage = moLiService.getGylqMessage(357078415L, false, 123456L);
        String jokeMessage = moLiService.getJokeMessage(357078415L, false, 123456L);
        String commonChatMessage = moLiService.getCommonChatMessage("你好呀");
        ReplyBlacklistInterceptor replyBlacklistInterceptor = ServiceFactory.getInstance().get(ReplyBlacklistInterceptor.class.getSimpleName(), ReplyBlacklistInterceptor.class);
        String replyBlacklist = PropertiesUtil.getProperty("application.properties","robot.reply.blacklist");
        System.out.println(replyBlacklist.contains(Long.toString(357078415L)));
        System.out.println("yllqMessage: " + yllqMessage);
        System.out.println("csylqMessage: " + csylqMessage);
        System.out.println("gylqMessage: " + gylqMessage);
        System.out.println("jokeMessage: " + jokeMessage);
        System.out.println("commonChatMessage: " + commonChatMessage);
        final FriendMessageEvent friendMessageEvent = new FriendMessageEvent(null, null, 1);
        SignInDataResponse signInDataResponse = signInService.doSignIn("357078415", "123456", 1);
        SignInDataResponse signInData = signInService.getSignInData("357078415", "123456", 1);
        String fortuneMsg = MessageFactory.getFortuneMsg(357078415L, 123456L, 1);
        System.out.println(fortuneMsg);
    }
}
