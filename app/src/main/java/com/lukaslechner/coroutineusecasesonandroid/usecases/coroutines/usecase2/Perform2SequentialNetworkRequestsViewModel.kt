package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch

/**
 * 1. written in pure imperative fashion and is executed sequentially
 * 2. implementation is much shorter
 * 3. you don't have to think about freeing resources, since there is a viewModelScope
 * 4. don't need to manually specify which code should run on which thread, since Retrofit calls
 * are main thread safe (libs are usually implemented this way) and Retrofit itself takes care
 * to execute in a background thread
 * */
class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {

        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val recentVersions = mockApi.getRecentAndroidVersions()
                val mostRecentVersion = recentVersions.last()
                val featuresOfMostRecentVersion = mockApi.getAndroidVersionFeatures(mostRecentVersion.apiLevel)
                uiState.value = UiState.Success(featuresOfMostRecentVersion)
            } catch (e: Exception) {
                uiState.value = UiState.Error("network request failed")
            }
        }

    }
}