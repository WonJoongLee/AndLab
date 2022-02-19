package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.Exception

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val oreoFeature = mockApi.getAndroidVersionFeatures(27)
                val pieFeatures = mockApi.getAndroidVersionFeatures(28)
                val android10Features = mockApi.getAndroidVersionFeatures(29)

                val versionFeatures = listOf(oreoFeature, pieFeatures, android10Features)
                uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception) {
                uiState.value = UiState.Error("Network Request Failed")
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading

        // 따로 start 파라미터 설정을하지 않았기 때문에, async를 만나는 즉시 실행된다.
        val oreoFeaturesDeferred = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(27)
        }
        val pieFeaturesDeferred = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(28)
        }
        val android10FeaturesDeferred = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(29)
        }

        viewModelScope.launch {
            try {
                // 위 async에서 즉시 실행했던 것이 끝날 때까지 기다린 후
                // 각 변수에 저장한다.
//                val oreoFeatures = oreoFeaturesDeferred.await()
//                val pieFeatures = pieFeaturesDeferred.await()
//                val android10Features = android10FeaturesDeferred.await()
                //val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)
                val versionFeatures = awaitAll(oreoFeaturesDeferred, pieFeaturesDeferred, android10FeaturesDeferred)
                uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }
}