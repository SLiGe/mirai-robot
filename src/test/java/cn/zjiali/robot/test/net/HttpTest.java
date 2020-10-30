package cn.zjiali.robot.test.net;

import cn.zjiali.robot.constant.ServerUrl;
import cn.zjiali.robot.util.HttpUtil;
import com.google.gson.JsonObject;

/**
 * @author zJiaLi
 * @since 2020-10-30 14:47
 */
public class HttpTest {

    public static void main(String[] args) {
        testSignIn();
    }


    private static void testSignIn(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq","357078415");
        jsonObject.addProperty("integral","20");
        String post = HttpUtil.httpPost(ServerUrl.SIGN_IN_URL, jsonObject);
        System.out.println(post);
    }
}
