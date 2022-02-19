package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {

    val startTime = System.currentTimeMillis()
    //val resultList = mutableListOf<String>()
    // networkCall(3)

    //val job1 = launch(start = CoroutineStart.LAZY) {
    val deferred1 = async(start = CoroutineStart.LAZY) {
        val result1 = networkCall(1)
        //resultList.add(result1)
        println("result1 received : $result1 after ${elapsedMillis(startTime)}")
        result1 // 반환 값
    }
    //val job2 = launch(start = CoroutineStart.LAZY) {
    val deferred2: Deferred<String> = async {
        val result2 = networkCall(2)
        //resultList.add(result2)
        println("result2 received : $result2 after ${elapsedMillis(startTime)}")
        result2 // 반환 값
    }

    //deferred1.join()
    //deferred1.cancel()
    val resultList = listOf(deferred1.await(), deferred2.await())

    //job1.join()
    //job2.join()
    println("Result list: $resultList after ${elapsedMillis(startTime)}ms")
}

suspend fun networkCall(number: Int): String {
    delay(500)
    return "Result $number"
}

// 시간이 얼마나 지났는지 확인할 수 있는 함수
fun elapsedMillis(startTime: Long) = System.currentTimeMillis() - startTime