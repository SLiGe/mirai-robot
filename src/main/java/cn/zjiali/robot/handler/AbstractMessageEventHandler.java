package cn.zjiali.robot.handler;

import cn.zjiali.robot.constant.AppConstants;
import cn.zjiali.robot.manager.PluginManager;
import cn.zjiali.robot.util.PluginConfigUtil;
import com.google.inject.Inject;
import net.mamoe.mirai.event.events.GroupMessageEvent;
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

    @Inject
    protected PluginManager pluginManager;

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

    /**
     * 消息中是否触发插件命令 - 服务端配置方式
     *
     * @param pluginCode 插件编码
     * @param event      消息事件
     * @return 是否触发插件命令
     */
    public boolean containCommand(String pluginCode, MessageEvent event) {
        String msg = event.getMessage().contentToString();
        if (event instanceof GroupMessageEvent) {
            long groupId = ((GroupMessageEvent) event).getGroup().getId();
            long senderId = event.getSender().getId();
            String command = pluginManager.getCommand(pluginCode, groupId, senderId);
            List<String> commandArray = Arrays.asList(command.split(","));
            return containCommand(msg, commandArray);
        }
        return containCommand(pluginCode, msg);
    }

    /**
     * 获取插件命令 - 服务端配置方式
     *
     * @param pluginCode 插件编码
     * @param event      消息事件
     * @return 插件命令
     */
    public String getCommand(String pluginCode, MessageEvent event) {
        if (event instanceof GroupMessageEvent) {
            long groupId = ((GroupMessageEvent) event).getGroup().getId();
            long senderId = event.getSender().getId();
            return pluginManager.getCommand(pluginCode, groupId, senderId);
        }
        return PluginConfigUtil.getCommand(pluginCode);
    }

    /**
     * 获取插件命令
     *
     * @param pluginCode 插件编码
     * @return 插件命令
     */
    public String getCommand(String pluginCode) {
        return PluginConfigUtil.getCommand(pluginCode);
    }

    /**
     * 获取插件请求地址 - 服务端配置方式
     *
     * @param pluginCode 插件编码
     * @param event      消息事件
     * @return 插件请求地址
     */
    public String getApiURL(String pluginCode, MessageEvent event) {
        if (event instanceof GroupMessageEvent) {
            long groupId = ((GroupMessageEvent) event).getGroup().getId();
            long senderId = event.getSender().getId();
            return pluginManager.getApiURL(pluginCode, groupId, senderId);
        }
        return PluginConfigUtil.getApiURL(pluginCode);
    }

    /**
     * 获取配置项值 - 服务端配置方式
     *
     * @param pluginCode 插件编码
     * @param key        配置项键名
     * @param groupId    群号
     * @param senderId   发送人号
     * @return 配置值
     */
    protected String getConfigVal(String pluginCode, String key, Long groupId, Long senderId) {
        return pluginManager.getConfigVal(pluginCode, key, groupId, senderId);
    }

    /**
     * 获取群号
     *
     * @param event 消息事件
     * @return 群号
     */
    protected Long groupId(MessageEvent event) {
        if (event instanceof GroupMessageEvent) {
            return ((GroupMessageEvent) event).getGroup().getId();
        }
        return null;
    }

    /**
     * 消息来源
     *
     * @param event 消息事件
     * @return 消息来源编码
     */
    protected int fromMsgType(MessageEvent event) {
        if (event instanceof GroupMessageEvent) {
            return AppConstants.MSG_FROM_GROUP;
        }
        return AppConstants.MSG_FROM_FRIEND;
    }


}
