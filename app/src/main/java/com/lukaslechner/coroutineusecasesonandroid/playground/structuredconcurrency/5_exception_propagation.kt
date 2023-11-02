package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.RuntimeException

/**
 * Job() - parent and siblings are cancelled
 * SupervisorJob() - parent and sibling are not cancelled
 * */
fun main() {

    val exceptionHander = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("caught exception $throwable")
    }
//    val scope = CoroutineScope(Job() + exceptionHander)
    val scope = CoroutineScope(SupervisorJob() + exceptionHander)
    // now this exceptionHandler will be called whenever a coroutine fails and its exception is not handled somewhere else

    scope.launch {
        println("coroutine 1 starts")
        delay(50)
        println("coroutine 1 fails")
        throw RuntimeException()
    }

    scope.launch {
        println("coroutine 2 starts")
        delay(500)
        println("coroutine 2 completed")
    }.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("coroutine 2 was cancelled")
        }
    }

    Thread.sleep(1000)

    println("scope was cancelled: ${!scope.isActive}")
}