package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("main starts")
    joinAll(
        async { coroutine(1, 500) }, // a coroutine
        async { coroutine(2, 300) } // another coroutine created with the coroutine builder
    )
    println("main ends")
}

/**
 * suspend - this type of functions can be suspended by the Kotlin coroutines machinery and later be resumed
 * without blocking the underlined thread in the meantime.
 * It can be suspended only on a suspension point inside its body, which is another suspend function.
 * */
suspend fun coroutine(number: Int, delay: Long) {
    println("coroutine $number starts to work")
    delay(delay)
    println("coroutine $number has finished")
}