package cn.zjiali.robot.handler;

import cn.zjiali.robot.annotation.Autowired;
import cn.zjiali.robot.config.plugin.SignInConfig;
import cn.zjiali.robot.entity.response.SignInDataResponse;
import cn.zjiali.robot.service.SignInService;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;


/**
 * @author zJiaLi
 * @since 2020-10-29 20:57
 */
public class SignInHandler implements Handler {

    @Autowired
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

    private String getSignInMsg(String message, long senderQQ, long groupNum, int msgType) {
        StringBuilder msgContentBuilder = new StringBuilder();
        if (message.contains("签到")) {
            SignInDataResponse response = signInService.doSignIn(Long.toString(senderQQ), Long.toString(groupNum), msgType);
            if (response == null) {
                return "签到服务异常,请联系管理员!";
            }
            int status = response.getStatus();
            //签到成功200  已经签到203
            if (status == 200) {
                SignInDataResponse.DataResponse dataResponse = response.getDataResponse();
                msgContentBuilder.append("签到成功！\n");
                msgContentBuilder.append("\uD83D\uDCB8获得积分：").append(response.getGetPoints()).append("点\n");
                msgContentBuilder.append("⭐本月积累签到：").append(dataResponse.getMonthDay()).append("天\n");
                msgContentBuilder.append("\uD83D\uDCB3当前积分：").append(dataResponse.getPoints()).append("点");
                if ("1".equals(SignInConfig.sign_in_cur_level)) {
                    msgContentBuilder.append("\n⭐当前等级:").append(dataResponse.getCurrentLevel());
                }
                if ("1".equals(SignInConfig.sign_in_day_sen)) {
                    msgContentBuilder.append("\n⭐每日一句:").append(dataResponse.getTodayMsg());
                }
            } else if (status == 203) {
                msgContentBuilder.append("你今天已经签到过了！");
            }
        } else if (message.contains("积分查询")) {
            SignInDataResponse response = signInService.getSignInData(Long.toString(senderQQ), Long.toString(groupNum), 2);
            if (response == null) {
                return "签到服务异常,请联系管理员!";
            }
            SignInDataResponse.DataResponse dataResponse = response.getDataResponse();
            msgContentBuilder.append("\uD83D\uDCB8总积分：").append(dataResponse.getPoints()).append("点\n");
            msgContentBuilder.append("⭐连续签到：").append(dataResponse.getMonthDay()).append("天\n");
            msgContentBuilder.append("\uD83D\uDCB3总签到天数：").append(dataResponse.getTotalDay()).append("天");
            if ("1".equals(SignInConfig.sign_in_cur_level)) {
                msgContentBuilder.append("\n⭐当前等级:").append(dataResponse.getCurrentLevel());
            }
            if ("1".equals(SignInConfig.sign_in_day_sen)) {
                msgContentBuilder.append("\n⭐每日一句:").append(dataResponse.getTodayMsg());
            }
        }
        return msgContentBuilder.toString();
    }
}
