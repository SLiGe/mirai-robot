package cn.zjiali.robot.service;

import cn.hutool.core.util.StrUtil;
import cn.zjiali.robot.annotation.Service;
import cn.zjiali.robot.config.AppConfig;
import cn.zjiali.robot.constant.MsgType;
import cn.zjiali.robot.factory.ServiceFactory;
import cn.zjiali.robot.manager.RobotManager;
import cn.zjiali.robot.model.response.ws.SenderMessageRes;
import cn.zjiali.robot.model.response.ws.WsResult;
import cn.zjiali.robot.util.CommonLogger;
import cn.zjiali.robot.util.JsonUtil;

/**
 * @author zJiaLi
 * @since 2021-07-30 15:15
 */
@Service
public class WebSocketService {

    private final RobotManager robotManager = ServiceFactory.getInstance().getBean(RobotManager.class.getSimpleName(), RobotManager.class);
    private final CommonLogger commonLogger = new CommonLogger(WebSocketService.class.getSimpleName());

    public void handleWsResult(WsResult wsResult) {
        String robotQQ = wsResult.getRobotQQ();
        if (StrUtil.isNotBlank(robotQQ) && AppConfig.getQQ().equals(robotQQ)) {
            if (wsResult.getMsgType() == MsgType.SEND_MSG) {
                String dataJson = wsResult.getDataJson();
                SenderMessageRes senderMessageRes = JsonUtil.json2obj(dataJson, SenderMessageRes.class);
                commonLogger.info("[WebSocket]====接收QQ:{} ,接收内容:{} ", senderMessageRes.getReceiver(), senderMessageRes.getSendMessage());
                if (senderMessageRes.getSendFlag() == 1) {
                    robotManager.sendFriendMessage(Long.parseLong(senderMessageRes.getReceiver()), senderMessageRes.getSendMessage());
                }
            }
        }
    }
}
