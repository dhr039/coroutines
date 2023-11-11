package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * You should install an exception handler only in the scope or in a TOP level coroutine.
 * In a nested(child) coroutine it will not have any effect.
 * CancellationException will also not get handled by the coroutine exception handler.
 * */
fun main() {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("caught $throwable in CoroutineExceptionHandler")
    }

    /** or could pass exceptionHandler to launch:  launch(exceptionHandler) {...}
     * if passing to both, then only the handler passed to launch will be called.
     * */
    val scope = CoroutineScope(Job() + exceptionHandler)

    scope.launch {
        throw RuntimeException()
    }

    Thread.sleep(100)
}