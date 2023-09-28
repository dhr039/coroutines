package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 1. we have to handle errors in 4 different places
 * 2. we must not forget to free resources to avoid memory leaks
 * 3. it is not clear on which Thread the onFailure() and onResponse() are called,
 * we have to go to the docs of Callback to see that the callbacks are executed
 * in the application's main UI Thread.
 * */
class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private var getAndroidVersionsCall: Call<List<AndroidVersion>>? = null
    private var getAndroidFeaturesCall: Call<VersionFeatures>? = null

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        getAndroidVersionsCall = mockApi.getRecentAndroidVersions()
        getAndroidVersionsCall!!.enqueue(object: Callback<List<AndroidVersion>>{

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                uiState.value = UiState.Error("something unexpected happened")
            }

            override fun onResponse(
                call: Call<List<AndroidVersion>>,
                response: Response<List<AndroidVersion>>
            ) {
                if(response.isSuccessful) {
                    val mostRecentVersion = response.body()!!.last()
                    getAndroidFeaturesCall = mockApi.getAndroidVersionFeatures(mostRecentVersion.apiLevel)
                    getAndroidFeaturesCall!!.enqueue(object : Callback<VersionFeatures>{


                        override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                            uiState.value = UiState.Error("something unexpected happened")
                        }

                        override fun onResponse(
                            call: Call<VersionFeatures>,
                            response: Response<VersionFeatures>
                        ) {
                            if(response.isSuccessful) {
                                val featuresOfMostRecentAndroidVersion = response.body()!!
                                uiState.value = UiState.Success(featuresOfMostRecentAndroidVersion)
                            } else {
                                uiState.value = UiState.Error("something unexpected happened")
                            }
                        }

                    })
                } else {
                    uiState.value = UiState.Error("network request failed")
                }
            }

        })
    }

    /**
     * If the user leaves the screen before the two network requests are completed they will continue
     * to run and cause a memory leak. This is because the Activity and the ViewModel won't get
     * garbage collected since the callbacks still hold a reference to the ViewModel. To fix this issue
     * we have to cancel the requests when the user leaves the screen. When the user leaves the screen
     * the onCleared() method of the ViewModel is called, so we need to cancel the network requests there.
     * However, we cannot access the call objects of the two network calls so we have to extract them to
     * member properties - now we can cancel them in the onCleared() method.
     * */
    override fun onCleared() {
        super.onCleared()

        getAndroidFeaturesCall?.cancel()
        getAndroidVersionsCall?.cancel()
    }
}