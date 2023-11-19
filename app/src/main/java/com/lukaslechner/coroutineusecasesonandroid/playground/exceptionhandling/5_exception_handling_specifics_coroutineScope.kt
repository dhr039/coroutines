package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    try {
        doSomethingSuspend()
    } catch (e: Exception) {
        println("Caught $e")
    }
}

private suspend fun doSomethingSuspend() {
    /**
     * a try/catch has no effect outside the launch{}, BUT, if we use a coroutineScope{} then
     * the exception is caught, the scoping function re-throws the exception and we are able to catch it
     * */
    coroutineScope {
        launch {
            throw RuntimeException()
        }
    }
}