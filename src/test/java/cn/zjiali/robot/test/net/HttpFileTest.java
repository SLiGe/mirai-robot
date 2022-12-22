package cn.zjiali.robot.test.net;

import cn.hutool.core.io.FileUtil;
import cn.zjiali.robot.util.HttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Path;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * @author zJiaLi
 * @since 2022-12-21 12:55
 */
public class HttpFileTest {

    @Test
    public void testStreamFile() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        byte[] inputStream = fileStream("https://api.zjiali.cn/file/preview/0f629af226074f9498af0f8c17c954a0", okHttpClient);
        Files.write(Paths.get("abc.jpeg"), inputStream, StandardOpenOption.CREATE);
    }

    public static byte[] fileStream(String url, OkHttpClient okHttpClient) {
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).bytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
