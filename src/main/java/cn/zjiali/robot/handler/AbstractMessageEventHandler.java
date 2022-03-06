package cn.zjiali.robot.handler;

import cn.zjiali.robot.util.PluginConfigUtil;
import net.mamoe.mirai.event.events.MessageEvent;

import java.util.Arrays;
import java.util.List;

/**
 * 处理器基类
 *
 * @author zJiaLi
 * @since 2021-04-08 21:11
 */
public abstract class AbstractMessageEventHandler implements MessageEventHandler {

    /**
     * 获取消息内容
     *
     * @param messageEvent 消息事件
     * @return 消息内容
     */
    public String getMsg(MessageEvent messageEvent) {
        return messageEvent.getMessage().contentToString();
    }


    /**
     * 消息中是否触发插件命令
     *
     * @param msg          消息内容
     * @param commandArray 命令集合
     * @return 是否触发插件命令
     */
    public boolean containCommand(String msg, List<String> commandArray) {
        for (String command : commandArray) {
            if (msg.contains(command))
                return true;
        }
        return false;
    }

    /**
     * 消息中是否触发插件命令
     *
     * @param pluginCode 插件编码
     * @param msg        消息内容
     * @return 是否触发插件命令
     */
    public boolean containCommand(String pluginCode, String msg) {
        String command = PluginConfigUtil.getCommand(pluginCode);
        List<String> commandArray = Arrays.asList(command.split(","));
        return containCommand(msg, commandArray);
    }
}
