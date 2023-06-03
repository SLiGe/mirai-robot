package cn.zjiali.robot.main.system;

import cn.hutool.core.codec.Base64;
import cn.zjiali.robot.util.*;
import com.google.gson.JsonObject;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.DeviceVerificationRequests;
import net.mamoe.mirai.utils.DeviceVerificationResult;
import net.mamoe.mirai.utils.LoginSolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Scanner;


/**
 * 登录解决器
 *
 * @author zJiaLi
 * @since 2021-04-17 15:06
 */
public class SysLoginSolver extends LoginSolver {


    private static final CommonLogger commonLogger = new CommonLogger(SysLoginSolver.class);

    private static final String VERIFY_CODE_VIEW_URL = "verifyCode.view.url";

    @Override
    public boolean isSliderCaptchaSupported() {
        return true;
    }

    @Nullable
    @Override
    public Object onSolvePicCaptcha(@NotNull Bot bot, byte[] bytes, @NotNull Continuation<? super String> continuation) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", String.valueOf(bot.getId()));
        jsonObject.addProperty("base64", Base64.encode(bytes));
        String responseJson = HttpUtil.post(PropertiesUtil.getApiProperty(VERIFY_CODE_VIEW_URL), jsonObject);
        JsonObject response = JsonUtil.json2obj(responseJson, JsonObject.class);
        String verifyCodeUrl = response.get("data").getAsString();
        commonLogger.warning("请打开以下网址后,在控制台输入验证码!");
        commonLogger.warning(verifyCodeUrl);
        for (int i = 0; i < 3; i++) {
            String code = new Scanner(System.in).nextLine();
            if (ObjectUtil.isNullOrEmpty(code)) continue;
            return code;
        }
        return null;
    }

    @Nullable
    @Override
    public Object onSolveSliderCaptcha(@NotNull Bot bot, @NotNull String s, @NotNull Continuation<? super String> continuation) {
        commonLogger.warning("请打开以下网址后,完成滑动验证码验证!");
        commonLogger.warning("完成后在控制台输入Ticket!");
        commonLogger.warning(s);
        return userVerify();
    }

    @Nullable
    @Override
    public Object onSolveDeviceVerification(@NotNull Bot bot, @NotNull DeviceVerificationRequests requests, @NotNull Continuation<? super DeviceVerificationResult> $completion) {
        DeviceVerificationRequests.FallbackRequest fallback = requests.getFallback();
        if (Objects.nonNull(fallback)) {
            String url = fallback.getUrl();
            commonLogger.warning("需要进行账户安全认证");
            commonLogger.warning("该账户有[设备锁]/[不常用登录地点]/[不常用设备登录]的问题");
            commonLogger.warning("请将该链接在浏览器中打开并完成认证, 成功后在控制台输入任意字符");
            commonLogger.warning(url);
            userVerify();
        }
        DeviceVerificationRequests.SmsRequest smsRequest = requests.getSms();
        if (Objects.nonNull(smsRequest)) {
            commonLogger.warning("需要进行短信验证:{},{}", smsRequest.getPhoneNumber(), smsRequest.getCountryCode());
            commonLogger.warning("输入send进行发送验证码消息");
            String code = null;
            while (true) {
                code = new Scanner(System.in).nextLine();
                if ("send".equals(code)) {
                    smsRequest.requestSms(new Continuation<>() {
                        @NotNull
                        @Override
                        public CoroutineContext getContext() {
                            return EmptyCoroutineContext.INSTANCE;
                        }

                        @Override
                        public void resumeWith(@NotNull Object o) {
                            commonLogger.info("验证结果: {}", o);
                        }
                    });
                } else {
                    return smsRequest.solved(code);
                }
            }

        }
        throw new RuntimeException("登录失败!");
    }

    private String userVerify() {
        String code = null;
        int num = 0;
        do {
            try {
                num++;
                code = new Scanner(System.in).nextLine();
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (ObjectUtil.isNullOrEmpty(code) && num <= 5);
        return code;
    }
}
