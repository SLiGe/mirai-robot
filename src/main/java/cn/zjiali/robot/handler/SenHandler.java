package cn.zjiali.robot.handler;

import cn.zjiali.robot.config.ServerUrlConfig;
import cn.zjiali.robot.net.HttpUtil;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;

/**
 * 一言处理器
 *
 * @author zJiaLi
 * @since 2020-10-29 20:57
 */
public class SenHandler implements Handler{

    @Override
    public void handleGroupMessage(GroupMessageEvent event) {
        String msgContent = event.getMessage().contentToString();
        if (msgContent.contains("一言")){
            String sen = HttpUtil.httpGet(ServerUrlConfig.SEN_URL);
            event.getGroup().sendMessage(sen);
        }

    }

    @Override
    public void handleFriendMessage(FriendMessageEvent event) {

    }
}
