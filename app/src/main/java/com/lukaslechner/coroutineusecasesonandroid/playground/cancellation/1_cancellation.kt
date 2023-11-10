package com.lukaslechner.coroutineusecasesonandroid.playground.cancellation

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch {
        repeat(10) {index ->
            println("operation number $index")
            delay(100)
        }
    }

    delay(250)
    println("cancelling coroutine")
    job.cancel()
}