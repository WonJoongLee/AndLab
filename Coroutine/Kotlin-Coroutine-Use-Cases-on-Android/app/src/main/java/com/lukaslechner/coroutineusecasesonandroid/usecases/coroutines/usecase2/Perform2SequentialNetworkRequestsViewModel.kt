package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        /**
         * viewmodel scope
         * - viewmodel에서 사용하는 scope
         * - 구글이 개발 및 관리
         * - 다른 쓰레도로 전환하지 않으면 Main Thread에서 작동한다. 그렇지만 non-blocking이다.
         * - 콜백이나 rxjava처럼 onCleared()에서 cancel해줄 필요가 없다는 것이 장점이기도 하다.
         */
        viewModelScope.launch {
            try {
                val recentAndroidVersions = mockApi.getRecentAndroidVersions()
                val recentAndroidVersionFeatures =
                    mockApi.getAndroidVersionFeatures(recentAndroidVersions.last().apiLevel)
                uiState.value = UiState.Success(recentAndroidVersionFeatures)
            } catch (exception: Exception) {
                Timber.e(exception)
                uiState.value = UiState.Error("Network request failed")
            }
        }
    }
}