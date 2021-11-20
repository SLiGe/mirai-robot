package cn.zjiali.robot.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 系统配置
 *
 * @author zJiaLi
 * @since 2021-09-04 12:23
 */
@Data
public class SystemConfig {

    private Map<String, List<String>> messageTemplates;
}
