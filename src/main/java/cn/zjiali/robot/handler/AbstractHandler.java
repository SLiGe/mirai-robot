package cn.zjiali.robot.handler;

import net.mamoe.mirai.event.events.MessageEvent;

/**
 * 处理器基类
 *
 * @author zJiaLi
 * @since 2021-04-08 21:11
 */
public abstract class AbstractHandler implements Handler{

    public String getMsg(MessageEvent messageEvent) {
        return messageEvent.getMessage().contentToString();
    }

}
