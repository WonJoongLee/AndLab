package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import java.lang.NullPointerException
import java.util.*
import kotlin.system.exitProcess

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        //myAnswer()
        teacherAnswer()
    }

    private fun teacherAnswer() {
        val numberOfRetries = 2
        val timeout = 1000L

        val oreoVersionDeferred = viewModelScope.async {
            retryWithTimeout(numberOfRetries, timeout){
                api.getAndroidVersionFeatures(27)
            }
        }
        val pieVersionDeferred = viewModelScope.async {
            retryWithTimeout(numberOfRetries, timeout){
                api.getAndroidVersionFeatures(28)
            }
        }

        viewModelScope.launch {
            try {
                val versionFeatures = listOf(oreoVersionDeferred, pieVersionDeferred).awaitAll()

                uiState.value = UiState.Success(versionFeatures)
            } catch (e:Exception){
                Timber.e(e)
                uiState.value = UiState.Error("Network Error")
            }
        }
    }

    private suspend fun <T> teacherRetry(
        numberOfRetries: Int,
        delayBetweenRetries: Long = 100,
        block: suspend () -> T
    ): T {
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (e: Exception) {
                Timber.e(e)
            }
            delay(delayBetweenRetries)
        }
        return block()
    }

    private suspend fun <T> retryWithTimeout(
        numberOfRetries: Int,
        timeout: Long,
        block: suspend () -> T
    ) = teacherRetry(numberOfRetries) {
        withTimeout(timeout) {
            block()
        }
    }

    private fun myAnswer() {
        val numberOfRetries = 2
        val timeout = 1000L

        val featuresDeferred1 = viewModelScope.async {
            retry(numberOfRetries, timeout) {
                api.getAndroidVersionFeatures(27)
            }
        }
        val featuresDeferred2 = viewModelScope.async {
            retry(numberOfRetries, timeout) {
                api.getAndroidVersionFeatures(28)
            }
        }
        viewModelScope.launch {
            val allVersionFeatures = awaitAll(
                featuresDeferred1,
                featuresDeferred2
            )
            uiState.value = UiState.Success(allVersionFeatures)
        }
    }

    private suspend fun <T> retry(
        numberOfRetries: Int,
        timeoutMillis: Long,
        initialDelayMillis: Long = 100,
        maxDelayMillis: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = 100L
        repeat(numberOfRetries) {
            try {
                return withTimeoutOrNull(timeoutMillis) {
                    block()
                } ?: throw NullPointerException("time out occurred null")
            } catch (e: Exception) {
                Timber.e(e)
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
        }
        return block()
    }
}