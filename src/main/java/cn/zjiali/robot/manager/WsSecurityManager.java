package cn.zjiali.robot.manager;

/**
 * @author zJiaLi
 * @since 2021-07-28 12:25
 */
public interface WsSecurityManager {

    /**
     * 生成客户端token
     *
     * @param content
     * @return
     */
    String genWsToken(String content);

    /**
     * 加密消息内容
     *
     * @param msgData
     * @return
     */
    String encryptMsgData(String msgData);

    /**
     * 解密消息内容
     *
     * @param msgData
     * @return
     */
    String decryptMsgData(String msgData);
}
