package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    // callback
    private var getAndroidVersionsCall: Call<List<AndroidVersion>>? = null
    private var getAndroidFeaturesCall: Call<VersionFeatures>? = null

    // rxjava
    private val disposables = CompositeDisposable()


    fun perform2SequentialNetworkRequest() {
        // usingCallback()
        // usingRxJava()
    }

    override fun onCleared() {
        super.onCleared()
        // usingCallbackAndCancelCallback() // 콜백을 사용할 때는 수동적으로 cancel을 해줘야 한다.
        // clearDisposableInRxJava() // 사용자가 화면을 떠나면 composite disposable을 clear해서 메모리릭 관련 이슈가 생기지 않도록 해야 한다.
    }

    private fun usingRxJava() {
        uiState.value = UiState.Loading
        mockApi.getRecentAndroidVersions()
            .flatMap { androidVersions ->
                val recentVersion = androidVersions.last()
                mockApi.getAndroidVersionFeatures(recentVersion.apiLevel)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // 라이브 데이터에 적용하기 위함
            .subscribeBy(
                onSuccess = { featureVersions ->
                    uiState.value = UiState.Success(featureVersions)
                },
                onError = {
                    uiState.value = UiState.Error("Network request failed!")
                }
            )
            .addTo(disposables)
    }


    private fun clearDisposableInRxJava() {
        disposables.clear()
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