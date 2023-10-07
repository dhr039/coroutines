package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {

    val startTime = System.currentTimeMillis()

    // async starts immediately, not on await()
    val deferred1 = async {
        val result1 = networkCall(1)
        println("result1 recieved $result1 after ${elapsedMillis(startTime)}")
        result1
    }
    // but can use LAZY if need to start it later with start():
    val deferred2 = async(start = CoroutineStart.LAZY) {
        val result2 = networkCall(2)
        println("result2 recieved $result2 after ${elapsedMillis(startTime)}")
        result2
    }
    // if you comment start(), then the deferred2 will start only at the await(), so it will take more time - will have to wait for deferred1 to finish
    deferred2.start()

    //by this time both jobs are already started and running, you just get the results with await (and maybe wait a bit more if needed)
    val resultList = listOf(deferred1.await(), deferred2.await())

    println("Result list: $resultList after ${elapsedMillis(startTime)}ms")

}

suspend fun networkCall(number: Int): String {
    delay(500)
    return "result $number"
}

fun elapsedMillis(startTime: Long) = System.currentTimeMillis() - startTime