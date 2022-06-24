package cn.zjiali.robot.handler;

import cn.zjiali.robot.config.PluginTemplate;
import cn.zjiali.robot.constant.AppConstants;
import cn.zjiali.robot.constant.MsgTemplate;
import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.model.response.SignInDataResponse;
import cn.zjiali.robot.service.SignInService;
import cn.zjiali.robot.util.MessageUtil;
import com.google.inject.Inject;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;


/**
 * 签到处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 20:57
 */
public class SignInMessageEventHandler extends AbstractMessageEventHandler {

    @Inject
    private SignInService signInService;


    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String message = event.getMessage().contentToString();
        long senderQQ = event.getSender().getId();
        long groupNum = event.getGroup().getId();
        String signInMsg = getSignInMsg(message, senderQQ, groupNum, 2);
        event.getGroup().sendMessage(new At(event.getSender().getId()).plus(signInMsg));
    }


    @Override
    public void handleFriendMessage(FriendMessageEvent event) {
        String message = event.getMessage().contentToString();
        long senderQQ = event.getSender().getId();
        String signInMsg = getSignInMsg(message, senderQQ, 0, 1);
        event.getSender().sendMessage(signInMsg);
    }

    @Override
    public OutMessage handleGroupMessageEvent(GroupMessageEvent event) {
        String message = event.getMessage().contentToString();
        long senderQQ = event.getSender().getId();
        long groupNum = event.getGroup().getId();
        return getSignInMsg2(message, senderQQ, groupNum, 2, event);
    }

    @Override
    public OutMessage handleFriendMessageEvent(FriendMessageEvent event) {
        String message = event.getMessage().contentToString();
        long senderQQ = event.getSender().getId();
        return getSignInMsg2(message, senderQQ, 0, 1, event);
    }

    private OutMessage getSignInMsg2(String message, long senderQQ, long groupNum, int msgType, MessageEvent event) {
        if (message.contains("签到")) {
            SignInDataResponse response = signInService.doSignIn(Long.toString(senderQQ), Long.toString(groupNum), msgType);
            if (response == null) {
                return OutMessage.builder().pluginCode(PluginCode.SIGN).convertFlag(false).content("签到服务异常,请联系管理员!").build();
            }
            int status = response.getStatus();
            //签到成功200  已经签到203
            if (status == 200) {
                SignInDataResponse.DataResponse dataResponse = response.getDataResponse();
                return OutMessage.builder().pluginCode(PluginCode.SIGN).convertFlag(true)
                        .fillFlag(AppConstants.FILL_OUT_MESSAGE_OBJECT_FLAG)
                        .templateCode(MsgTemplate.SIGN_TEMPLATE).fillObj(dataResponse).build();
            } else if (status == 203) {
                return OutMessage.builder().pluginCode(PluginCode.SIGN)
                        .convertFlag(false).content("你今天已经签到过了！").build();
            }
        } else if (message.contains("积分查询")) {
            SignInDataResponse response = signInService.getSignInData(Long.toString(senderQQ), Long.toString(groupNum), 2);
            if (response == null) {
                return OutMessage.builder().pluginCode(PluginCode.SIGN).convertFlag(false).content("签到服务异常,请联系管理员!").build();
            }
            SignInDataResponse.DataResponse dataResponse = response.getDataResponse();
            return OutMessage.builder().convertFlag(true)
                    .pluginCode(PluginCode.SIGN)
                    .event(event)
                    .fillFlag(AppConstants.FILL_OUT_MESSAGE_OBJECT_FLAG)
                    .templateCode(MsgTemplate.QUERY_SIGN_TEMPLATE).fillObj(dataResponse).build();
        }
        return null;
    }

    private String getSignInMsg(String message, long senderQQ, long groupNum, int msgType) {
        String msgContentBuilder = "";
        if (message.contains("签到")) {
            SignInDataResponse response = signInService.doSignIn(Long.toString(senderQQ), Long.toString(groupNum), msgType);
            if (response == null) {
                return "签到服务异常,请联系管理员!";
            }
            int status = response.getStatus();
            //签到成功200  已经签到203
            if (status == 200) {
                SignInDataResponse.DataResponse dataResponse = response.getDataResponse();
                return MessageUtil.replaceMessage(PluginTemplate.getInstance().getTemplate(MsgTemplate.SIGN_TEMPLATE), dataResponse);
            } else if (status == 203) {
                return "你今天已经签到过了！";
            }
        } else if (message.contains("积分查询")) {
            SignInDataResponse response = signInService.getSignInData(Long.toString(senderQQ), Long.toString(groupNum), 2);
            if (response == null) {
                return "签到服务异常,请联系管理员!";
            }
            SignInDataResponse.DataResponse dataResponse = response.getDataResponse();
            return MessageUtil.replaceMessage(PluginTemplate.getInstance().getTemplate(MsgTemplate.QUERY_SIGN_TEMPLATE), dataResponse);
        }
        return msgContentBuilder;
    }

    @Override
    public boolean next() {
        return false;
    }

    @Override
    public boolean matchCommand(String msg) {
        return containCommand(PluginCode.SIGN, msg);
    }

    @Override
    public String code() {
        return PluginCode.SIGN;
    }
}
