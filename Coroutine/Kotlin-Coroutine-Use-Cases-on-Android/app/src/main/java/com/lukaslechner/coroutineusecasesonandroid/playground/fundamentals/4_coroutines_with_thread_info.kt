package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("main starts")
    joinAll(
        // async는 코루틴 빌더 중 하나다.
        async { coroutine(1, 500) },
        async { coroutine(2, 300) }
    )
    println("main ends")
}

suspend fun coroutineWithThreadInfo(number: Int, delay: Long) {
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    // delay() 함수 또한 suspend function이기 때문에, coroutine() 함수가
    // suspend 함수가 아니라면 실행하지 못한다.
    delay(delay)
    println("Coroutine $number has finished ${Thread.currentThread().name}")
}
