package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

/**
 * 05-003, 05-004, 05-005
 * could run the code concurrently also by still using .launch,
 * but then we would have shared mutable state, which should be avoided.
 * */
class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val oreoFeatures = mockApi.getAndroidVersionFeatures(27)
                val pieFeatures = mockApi.getAndroidVersionFeatures(28)
                val android10Features = mockApi.getAndroidVersionFeatures(29)

                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)
                uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception) {
                uiState.value = UiState.Error(e.toString())
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading
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
                /*val oreoFeatures = oreoFeaturesDeferred.await()
                val pieFeatures = pieFeaturesDeferred.await()
                val android10Features = android10FeaturesDeferred.await()
                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)*/

                val versionFeatures = awaitAll(oreoFeaturesDeferred,pieFeaturesDeferred, android10FeaturesDeferred)

                uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception) {
                uiState.value = UiState.Error(e.toString())
            }
        }

    }
}