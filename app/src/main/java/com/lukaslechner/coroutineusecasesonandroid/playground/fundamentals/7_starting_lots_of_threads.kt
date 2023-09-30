package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlin.concurrent.thread

/**
 * btw this code is not giving an OutOfMemoryError anymore,
 * just works a lot more slowly than the coroutine version.
 * Before, at some point, the system run out of memory and wasn't able
 * to allocate an additional Thread.
 * */
fun main() {
    repeat(1_000_000) {
        thread {
            Thread.sleep(5000)
            print(".")
        }
    }
}