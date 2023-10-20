package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class VariableAmountOfNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val recentVersions = mockApi.getRecentAndroidVersions()
                val versionFeatures = recentVersions.map { androidVersion ->
                    mockApi.getAndroidVersionFeatures(androidVersion.apiLevel)
                }
                uiState.value = UiState.Success(versionFeatures)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("network request failed")
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            val recentVersion = mockApi.getRecentAndroidVersions()

            try {
                val result = recentVersion.map { androidVersion ->
                    async {
                        mockApi.getAndroidVersionFeatures(androidVersion.apiLevel)
                    }
                }.awaitAll()
                uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                uiState.value = UiState.Error(e.toString())
            }

        }
    }
}