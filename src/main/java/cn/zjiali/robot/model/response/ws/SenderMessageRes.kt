package cn.zjiali.robot.model.response.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zJiaLi
 * @since 2021-07-30 15:57
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SenderMessageRes {

    /**
     * 接收人
     */
    private String receiver;
    /**
     * 发送人
     */
    private String sender;
    /**
     * 发送消息
     */
    private String sendMessage;
    /**
     * 发送群组可空
     */
    private String sendGroup;
    /**
     * 发送类型: 1 好友 2 群组
     */
    private Integer sendFlag;

}
