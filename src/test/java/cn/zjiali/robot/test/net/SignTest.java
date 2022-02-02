package cn.zjiali.robot.test.net;

import cn.zjiali.robot.factory.ServiceFactory;
import cn.zjiali.robot.handler.SpiritSignMessageEventHandler;
import cn.zjiali.robot.main.ApplicationBootStrap;
import cn.zjiali.robot.main.OutMessageConvert;
import cn.zjiali.robot.model.message.OutMessage;
import org.junit.Test;

import java.io.IOException;

/**
 * @author zJiaLi
 * @since 2022-01-31 23:54
 */
public class SignTest {

    @Test
    public void testSign() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        ApplicationBootStrap.getInstance().init();
        SpiritSignMessageEventHandler spiritSignMessageEventHandler = new SpiritSignMessageEventHandler();
        OutMessage gyMessage = spiritSignMessageEventHandler.signPerDay("观音灵签", "357078415");
        String gy = OutMessageConvert.getInstance().convert(gyMessage);
        System.out.println(gy);
        System.out.println();
        OutMessage ylMessage = spiritSignMessageEventHandler.signPerDay("月老灵签", "357078415");
        String yl = OutMessageConvert.getInstance().convert(ylMessage);
        System.out.println(yl);
        System.out.println();
        OutMessage csMessage = spiritSignMessageEventHandler.signPerDay("财神灵签", "357078415");
        String cs = OutMessageConvert.getInstance().convert(csMessage);
        System.out.println(cs);
    }
}
