package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    /*this is the code before implementing a higher order function for retry:*/
//    fun performNetworkRequest() {
//        uiState.value = UiState.Loading
//
//        viewModelScope.launch {
//            val numberOfRetries = 2
//
//            try {
//                repeat(numberOfRetries) {
//                    /*if getRecentAndroidVersions() fails, it will exit the repeat(){} block, that's why we need another try/catch*/
//                    try {
//                        loadRecentAndroidVersions()
//                        /*in case of success we leave the repeat(){} block so that no unnecessary request are performed:*/
//                        return@launch // leave the whole coroutine, not just the repeat
//                    } catch (e: Exception) {
//                        Timber.e(e)
//                    }
//                }
//                /*we have to try another request after the repeat(){} block, to be able to propagate to the outer catch:*/
//                loadRecentAndroidVersions()
//            } catch (e: Exception) {
//                Timber.e(e)
//                uiState.value = UiState.Error(e.toString())
//            }
//        }
//    }


    /*this is after we implementing a higher order function for retry:*/
    fun performNetworkRequest() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            val numberOfRetries = 2
            try {
                retry(numberOfRetries) {
                    loadRecentAndroidVersions()
                }
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error(e.toString())
            }
        }
    }

    /*we also want to return the result of the block() while still keeping it generic
    * so we have to introduce a new Generic type <T> */
    private suspend fun <T> retry(numberOfRetries: Int, block: suspend () -> T): T {
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        return block()
    }

    private suspend fun loadRecentAndroidVersions() {
        val recentAndroidVersions = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(recentAndroidVersions)
    }

}