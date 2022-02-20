package cn.zjiali.robot.test.websocket

import kotlinx.coroutines.*
import org.junit.Test

/**
 *
 *
 * @author zJiaLi
 * @since 2022-02-20 10:58
 */

@OptIn(DelicateCoroutinesApi::class)
fun main(args: Array<String>){
    val launch = GlobalScope.launch(Dispatchers.Unconfined) {
        Run().pring()
    }
    //launch.start()

}

class Run {

    suspend fun pring(): Unit {
        println("111")
    }



    @Test
    suspend fun testRun() {
      withContext(Dispatchers.Main){
          Run().pring()
      }

    }
}