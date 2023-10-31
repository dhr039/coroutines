package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val scope = CoroutineScope(Dispatchers.Default)
    val parentCoroutineJob = scope.launch {
        launch {
            delay(1000)
            println("child coroutine 1 completed")
        }
        launch {
            delay(1000)
            println("child coroutine 2 completed")
        }
    }

    parentCoroutineJob.join()

    // before the parent coroutine is completed, its child coroutines must first complete, so we see this println as the last one:
    println("parent coroutineis completed")
}