package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        viewModelScope.launch {
            val numberOfRetries = 1 // 두번 반복
            try {
                retry(numberOfRetries) {
                    loadRecentAndroidVersions()
                }
            } catch (exception: Exception) {
                Timber.e(exception)
                uiState.value = UiState.Error("Network request failed!")
            }
        }
    }

    private suspend fun <T> retry(
        numberOfRetries: Int,
        initialDelayMillis: Long = 100, // delay 초기값
        maxDelayMillis: Long = 1000, // delay 최대값
        factor: Double = 2.0, // delay 몇 배로 증가할 것인지
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelayMillis
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (exception: Exception) {
                Timber.e(exception)
            }
            delay(currentDelay)
            // coerceAtMost를 사용하는 이유는 maxDelayMillis를 넘지 않게 하기 위해서다.
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
        }
        return block()
    }

    private suspend fun loadRecentAndroidVersions() {
        val recentAndroidVersions = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(recentAndroidVersions)
    }

}