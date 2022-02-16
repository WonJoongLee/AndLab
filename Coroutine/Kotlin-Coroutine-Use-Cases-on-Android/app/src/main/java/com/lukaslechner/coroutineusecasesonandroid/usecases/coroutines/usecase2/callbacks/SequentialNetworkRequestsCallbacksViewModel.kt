package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private var getAndroidVersionsCall: Call<List<AndroidVersion>>? = null
    private var getAndroidFeaturesCall: Call<VersionFeatures>? = null

    fun perform2SequentialNetworkRequest() {
        // usingCallback()

    }

    override fun onCleared() {
        super.onCleared()
        usingCallbackAndCancelCallback() // 콜백을 사용할 때는 수동적으로 cancel을 해줘야 한다.
    }

    private fun usingCallback() {
        uiState.value = UiState.Loading

        getAndroidVersionsCall!!.enqueue(object : Callback<List<AndroidVersion>> {
            override fun onResponse(
                call: Call<List<AndroidVersion>>,
                response: Response<List<AndroidVersion>>
            ) {
                if (response.isSuccessful) { // 200으로 떨어질 때
                    getAndroidFeaturesCall!!.enqueue(object : Callback<VersionFeatures> {
                        override fun onResponse(
                            call: Call<VersionFeatures>,
                            response: Response<VersionFeatures>
                        ) {
                            val featuresOfMostRecentVersion = response.body()!!
                            uiState.value = UiState.Success(featuresOfMostRecentVersion)
                        }

                        override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                            uiState.value = UiState.Error("Network request failed")
                        }

                    })
                } else { // 400, 500일 때
                    uiState.value = UiState.Error("Network request failed")
                }
            }

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                uiState.value = UiState.Error("Something unexpected happened!")
            }
        })
    }

    private fun usingCallbackAndCancelCallback() {
        getAndroidVersionsCall?.cancel()
        getAndroidFeaturesCall?.cancel()
    }
}