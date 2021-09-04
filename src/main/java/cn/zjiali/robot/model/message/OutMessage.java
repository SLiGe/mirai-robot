package cn.zjiali.robot.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author zJiaLi
 * @since 2021-09-04 11:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutMessage {

    /**
     * 消息内容
     */
    private String content;

    /**
     * 转换标志
     */
    private boolean convertFlag;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 填充消息标志 --> 1. fillMap 2. fillObj
     */
    private int fillFlag;

    /**
     * 填充Map
     */
    private Map<String, String> fillMap;

    /**
     * 填充对象
     */
    private Object fillObj;
}
