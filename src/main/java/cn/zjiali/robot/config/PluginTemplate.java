package cn.zjiali.robot.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 插件消息模板
 *
 * @author zJiaLi
 * @since 2021-09-04 12:06
 */
public class PluginTemplate {

    private final static PluginTemplate instance = new PluginTemplate();
    private final Map<String, String> templateMap = new HashMap<>();

    public static PluginTemplate getInstance() {
        return instance;
    }

    public void putTemplate(String templateCode, String templateText) {
        this.templateMap.put(templateCode, templateText);
    }

    public String getTemplate(String templateCode) {
        return this.templateMap.get(templateCode);
    }

}
