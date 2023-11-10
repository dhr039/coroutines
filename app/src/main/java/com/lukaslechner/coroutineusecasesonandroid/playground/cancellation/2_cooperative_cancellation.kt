package com.lukaslechner.coroutineusecasesonandroid.playground.cancellation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield


/**
 * when we are using our own functions we have to make sure that we respect the cooperative cancellation
 * and there are several options:
 * 1. ensureActive() - basically does the same what delay() does
 * 2. yield() - its purpose is to give other coroutines a chance to run
 * 3. if(isActive) {...} else {return@launch or throw CancellationException}
 * the benefit of isActive is that we can perform some cleanup operation before
 * throwing a CancellationException, shutdown the database for example.
 * */
fun main() = runBlocking {
    val job = launch(Dispatchers.Default) {
        repeat(10) {index ->
//            ensureActive()
            yield()
            println("operation number $index")
            Thread.sleep(100)
        }
    }

    delay(250)
    println("cancelling the coroutine")
    job.cancel()

}