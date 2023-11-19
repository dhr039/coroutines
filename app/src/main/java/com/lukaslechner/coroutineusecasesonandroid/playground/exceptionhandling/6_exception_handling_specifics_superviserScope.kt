package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

fun main() = runBlocking<Unit> {
    try {
        /**
         * Unlike with the coroutineScope{}, the supervisorScope is NOT re-throwing and
         * the exception is not caught. This is because the exceptions will not be
         * propagated above SupervisorJobs. If the exception occurs directly in the supervisorScope
         * (without/outside the launch{}) then it WILL be caught. And if it fails like this, then all
         * of the started coroutines will be cancelled.
         * */
        supervisorScope {
            launch {
                throw RuntimeException()
            }
        }
    } catch (e: Exception) {
        println("Caught $e")
    }
}