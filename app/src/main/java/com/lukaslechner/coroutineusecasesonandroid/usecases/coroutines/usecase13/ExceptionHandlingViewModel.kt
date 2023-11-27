package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase13

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.*
import retrofit2.HttpException
import timber.log.Timber

class ExceptionHandlingViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun handleExceptionWithTryCatch() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                api.getAndroidVersionFeatures(27)
            } catch (e: Exception) {
                if (e is HttpException) {
                    if (e.code() == 500) {
                        //error msg 1
                    } else {
                        // error msg 2
                    }
                }
                uiState.value = UiState.Error("network request failed $e")
            }
        }
    }

    /**
     * if we want to recover from the error, for example do a retry, then we should use try/catch
     * otherwise it is fine to use an CoroutineExceptionHandler
     * */
    fun handleWithCoroutineExceptionHandler() {
        uiState.value = UiState.Loading

        val exceptionHandler = CoroutineExceptionHandler {coroutineContext, throwable ->
            uiState.value = UiState.Error("network request failed")
        }

        viewModelScope.launch(exceptionHandler) {
            api.getAndroidVersionFeatures(27)
        }
    }

    fun showResultsEvenIfChildCoroutineFails() {

    }
}