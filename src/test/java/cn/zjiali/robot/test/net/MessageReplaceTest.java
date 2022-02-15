package cn.zjiali.robot.test.net;

import cn.zjiali.robot.model.response.YellowCalendarResponse;
import cn.zjiali.robot.util.MessageUtil;
import org.junit.Test;

/**
 * @author zJiaLi
 * @since 2022-02-15 15:45
 */
public class MessageReplaceTest {

    @Test
    public void testReplaceWithObj() {
        String message = "今日老黄历:\n阳历:{yangli}\n阴历:{yinli}\n五行:{wuxing}\n冲煞:{chongsha}\n彭祖百忌:{baiji}\n吉神宜趋:{jishen}\n宜:{yi}\n凶神宜忌:{xiongshen}\n忌:{ji}";
        YellowCalendarResponse yellowCalendarResponse = new YellowCalendarResponse();
        yellowCalendarResponse.setBaiji("111");
        yellowCalendarResponse.setYangli("111");
        String message1 = MessageUtil.replaceMessage(message, yellowCalendarResponse);
        System.out.println(message1);
    }
}
