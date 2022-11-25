package cn.zjiali.robot.constant

import kotlinx.serialization.json.Json

/**
 * @author zJiaLi
 * @since 2021-09-07 10:20
 */
object Constants {

    const val Y = "1"

    const val N = "0"

    val JSON = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

}