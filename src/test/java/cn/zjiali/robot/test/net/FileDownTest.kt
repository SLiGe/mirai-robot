package cn.zjiali.robot.test.net

import cn.zjiali.robot.util.HttpUtil
import okhttp3.Request
import org.junit.Test
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*
import kotlin.io.path.inputStream

/**
 *
 * @author zJiaLi
 * @since 2022-12-23 14:19
 */
class FileDownTest {

    @Test
    fun downImage() {
        val tempFileName = "E:\\tmp\\tempDir\\" + "柯南00145-哦.jpg"
        try {
            val imageStream =
                imageStream(
                    "柯南00145-哦.jpg",
                    "https://server.zjiali.cn/file/preview/19691bc88101b01b404368146a107342"
                )
            println(imageStream.readAllBytes())
            Thread.sleep(5000)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {

            Files.delete(Path.of("E:", "tmp", "tempDir", "柯南00145-哦.jpg"))
        }

    }

    private fun imageStream(imaName: String, imgUrl: String?): InputStream {
        val fileBytes = HttpUtil.fileBytes(imgUrl)
        Files.exists(Path.of("E:", "tmp", "tempDir")).let {
            if (!it) {
                Files.createDirectory(Path.of("E:", "tmp", "tempDir"))
            }
        }
        val createFile = Files.createFile(Path.of("E:", "tmp", "tempDir", imaName))
        return Files.write(createFile, fileBytes, StandardOpenOption.WRITE)
            .inputStream()
    }
}