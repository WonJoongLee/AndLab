package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.*

fun main() = runBlocking {
    println("main starts")
    joinAll(
        // async는 코루틴 빌더 중 하나다.
        async { threadsSwitchingCoroutine(1, 500) },
        async { threadsSwitchingCoroutine(2, 300) }
    )
    println("main ends")
}

suspend fun threadsSwitchingCoroutine(number: Int, delay: Long) {
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    // delay() 함수 또한 suspend function이기 때문에, coroutine() 함수가
    // suspend 함수가 아니라면 실행하지 못한다.
    delay(delay)
    withContext(Dispatchers.Default) {
        println("Coroutine $number has finished ${Thread.currentThread().name}")
    }
}
