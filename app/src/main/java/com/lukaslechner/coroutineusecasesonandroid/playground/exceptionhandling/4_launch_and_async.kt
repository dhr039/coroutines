package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.RuntimeException

/**
 * with nested coroutines exception propagation gets more complicated, see details in the video 11 - 005
 * */
fun main() {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler!")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

    /*exception is not thrown directly in the async, instead async holds the exception
    * and re-throws it when 'await' is called*/
    val deferred = scope.async {
        delay(200)
        throw RuntimeException()
    }

    scope.launch {
        deferred.await()
    }

    Thread.sleep(500)
}
