package cn.zjiali.robot.test.encrypt;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class EncryptTest {

    @Test
    public void testAes(){
        AES aes = SecureUtil.aes("D1cs+7H5OXaL9ATl".getBytes(StandardCharsets.UTF_8));
        String a = aes.encryptHex("msgData", StandardCharsets.UTF_8);
        System.out.println(a);
    }
}
