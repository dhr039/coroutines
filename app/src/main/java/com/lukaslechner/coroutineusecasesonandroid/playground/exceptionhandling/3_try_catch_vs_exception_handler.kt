package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.RuntimeException

/**
 * if the 2 coroutines are totally unrelated, then it makes sense to catch in one of them
 * and if there is an exception, the other coroutine will continue. But it doesn't make any sense
 * if coroutines are closely related and one doesn't make sense without the other. Then to avoid
 * wasting resources we could just use an exception handler for both.
 *
 * When you catch inside the coroutine remember that the cancellation is not propagated.
 * */
fun main() {

    val exceptionHandler = CoroutineExceptionHandler {coroutineContext, throwable ->
        println("caught exception: $throwable")
    }

    val scope = CoroutineScope(Job())
    scope.launch(exceptionHandler) {

        launch {
            println("starting coroutine 1")
            delay(100)
            throw RuntimeException()
//            try {
//                throw RuntimeException()
//            } catch (e: Exception) {
//                println("caught exception $e")
//            }
        }

        launch {
            println("starting coroutine 2")
            delay(3000)
            println("coroutine 2 completed")
        }
    }

    Thread.sleep(5000)
}