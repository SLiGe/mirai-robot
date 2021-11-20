package cn.zjiali.robot.model.response.ws;

import lombok.Builder;
import lombok.Data;

/**
 * @author zJiaLi
 * @since 2021-07-28 20:09
 */
@Data
@Builder
public class WsResult {

    /**
     * 消息类型
     */
    private int msgType;

    /**
     * 传递数据
     */
    private String dataJson;

    /**
     * 机器人QQ
     */
    private String robotQQ;

    /**
     * 时间戳
     */
    private long timestamp;

}
