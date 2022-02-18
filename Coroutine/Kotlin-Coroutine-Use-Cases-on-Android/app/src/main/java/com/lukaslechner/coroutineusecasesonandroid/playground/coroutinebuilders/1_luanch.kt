package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.*

fun main() {
    usingRunBlocking()
}

suspend fun networkRequest(): String {
    delay(500)
    return "Result"
}

private fun usingRunBlocking() = runBlocking<Unit> {
    val job = launch(start = CoroutineStart.LAZY) { // launch로 시작했기 때문에 background에서 돈다.
        networkRequest()
        println("result received")
    }
    delay(200)
    job.start()
    //job.join()
    println("end of runBlocking")
}

private fun usingGlobalScope() = GlobalScope.launch {
    delay(500)
    println("printed from within Coroutine")
}
