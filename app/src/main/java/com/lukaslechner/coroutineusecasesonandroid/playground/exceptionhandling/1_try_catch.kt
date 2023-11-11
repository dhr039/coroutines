package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * If a coroutine doesn't catch an exception it doesn't re-throw it in a traditional way,
 * as a regular function would. It uses another mechanism by propagating the exception
 * up the job hierarchy. And then they can be handled with a coroutine exception handler.
 * In other words, if it fails inside the coroutine body, the error is not propagated
 * and you cannot catch it somewhere else. Instead you should use coroutine exception handler
 * (as a last resort)
 * */
fun main() {
    val scope = CoroutineScope(Job())

    scope.launch {
        try {
            launch {
                /*this is not caught:*/
                functionThatThrows()
            }
            functionThatThrows()
        } catch (e: Exception) {
            println("Caught: $e")
        }
    }

    Thread.sleep(100)

}

private fun functionThatThrows() {
    throw RuntimeException()
}