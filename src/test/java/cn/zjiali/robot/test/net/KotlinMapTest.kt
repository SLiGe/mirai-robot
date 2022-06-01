package cn.zjiali.robot.test.net

import org.junit.Test

/**
 *
 * @author zJiaLi
 * @since 2022-02-15 16:22
 */
class KotlinMapTest {

    @Test
    fun testMap(): Unit {
        val queryReplyBlacklistParamMap: MutableMap<String, Any> = mutableMapOf()
        queryReplyBlacklistParamMap["1"]= "1"
        println(queryReplyBlacklistParamMap["1"])

        val songName = "点歌 我是"
        println(songName.startsWith("点歌"))
        println(songName.replace("点歌",""))
    }
}