package cn.zjiali.robot.manager;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.zjiali.robot.annotation.Autowired;
import cn.zjiali.robot.annotation.Component;
import cn.zjiali.robot.service.DictService;
import cn.zjiali.robot.util.CommonLogger;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author zJiaLi
 * @since 2021-07-28 12:36
 */
@Component(name = "DefaultWsSecurityManager")
public class DefaultWsSecurityManager implements WsSecurityManager {

    private final CommonLogger commonLogger = new CommonLogger(WsSecurityManager.class.getSimpleName());
    @Autowired
    private DictService dictService;
    private final Map<String, Object> queryVerifyKeyParamMap = Maps.newHashMap();
    private final Map<String, Object> queryMessageEncryptKeyParamMap = Maps.newHashMap();

    public DefaultWsSecurityManager() {
        queryVerifyKeyParamMap.put("dictCode", "verifyKey");
        queryVerifyKeyParamMap.put("dictTypeCode", "D00002");
        queryMessageEncryptKeyParamMap.put("dictTypeCode", "D00002");
        queryMessageEncryptKeyParamMap.put("dictCode", "messageEncryptKey");
    }

    @SneakyThrows
    @Override
    public String genWsToken(String content) {
        String verifyKey = dictService.getDictVal(queryVerifyKeyParamMap);
        String verifyToken = SecureUtil.md5(content + verifyKey);
        commonLogger.info("[Websocket] === 生成客户端Token robotQQ: {},生成结果: {}", content, verifyToken);
        return verifyToken;
    }

    /**
     * 加密消息内容
     *
     * @param msgData
     * @return
     */
    @SneakyThrows
    @Override
    public String encryptMsgData(String msgData) {
        String messageEncryptKey = dictService.getDictVal(queryMessageEncryptKeyParamMap);
        AES aes = SecureUtil.aes(messageEncryptKey.getBytes(StandardCharsets.UTF_8));
        return aes.encryptHex(msgData, StandardCharsets.UTF_8);
    }

    /**
     * 解密消息内容
     *
     * @param msgData
     * @return
     */
    @SneakyThrows
    @Override
    public String decryptMsgData(String msgData) {
        String messageEncryptKey = dictService.getDictVal(queryMessageEncryptKeyParamMap);
        AES aes = SecureUtil.aes(messageEncryptKey.getBytes(StandardCharsets.UTF_8));
        return aes.decryptStr(msgData, StandardCharsets.UTF_8);
    }
}
