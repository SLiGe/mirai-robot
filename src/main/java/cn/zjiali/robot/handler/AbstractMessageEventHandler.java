package cn.zjiali.robot.handler;

import cn.zjiali.robot.config.plugin.PluginConfig;
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

    public String getMsg(MessageEvent messageEvent) {
        return messageEvent.getMessage().contentToString();
    }


    public boolean containCommand(String msg, List<String> commandArray) {
        for (String command : commandArray) {
            if (msg.contains(command))
                return true;
        }
        return false;
    }

    public boolean containCommand(String pluginCode,String msg) {
        String command = PluginConfig.getCommand(pluginCode);
        List<String> commandArray = Arrays.asList(command.split(","));
        return containCommand(msg,commandArray);
    }
}
