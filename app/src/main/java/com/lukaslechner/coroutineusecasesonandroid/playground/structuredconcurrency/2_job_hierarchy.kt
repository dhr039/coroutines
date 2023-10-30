package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    val scopeJob = Job()
    val scope = CoroutineScope(Dispatchers.Default + scopeJob)

    val passedJob = Job()
    // NOT recommended to pass jobs in the context parameter:
    val coroutineJob = scope.launch(passedJob) {
        println("starting the coroutine")
        delay(1000)
    }

    Thread.sleep(100)

    println("passedJob and coroutineJob are references to the same object? => ${passedJob === coroutineJob}")
    println("is coroutine job a child of scope job? => ${scopeJob.children.contains(coroutineJob)}")
}