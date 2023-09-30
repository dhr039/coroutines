package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlin.concurrent.thread

/**
 * an allocated Thread object consumes a considerable amount of memory
 * and switching between Threads is a very expensive operation
 * */
fun main() {
    println("main starts")
    threadRoutine(1, 500)
    threadRoutine(2, 300)
    Thread.sleep(1000)
    println("main ends")
}

fun threadRoutine(number: Int, delay: Long) {
    thread {
        println("routine $number starts to work")
        Thread.sleep(delay)
        println("routine $number has finished")
    }
}