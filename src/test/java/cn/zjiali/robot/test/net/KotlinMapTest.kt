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
    }
}