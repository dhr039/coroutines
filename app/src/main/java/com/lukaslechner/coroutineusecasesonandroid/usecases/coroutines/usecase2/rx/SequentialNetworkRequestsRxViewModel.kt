package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.rx

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * 1. has 20 lines of code less than the callback version
 * 2. it's not so deeply nested - easier to read/understand
 * 3. error is handled in one place only
 * 4. still have to clear resources manually, but it is easier than
 * in the callback version since we don't have to clear each network request
 * 5. you need to learn/understand RXJava operators
 * */
class SequentialNetworkRequestsRxViewModel(
    private val mockApi: RxMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val disposables = CompositeDisposable()

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        mockApi.getRecentAndroidVersions()
            .flatMap {androidVersions ->
                val recentVersion = androidVersions.last()
                mockApi.getAndroidVersionFeatures(recentVersion.apiLevel)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {featureVersions ->
                    uiState.value = UiState.Success(featureVersions)
                },
                onError = {
                    uiState.value = UiState.Error("network request failed")
                }
            )
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()

        disposables.clear()
    }
}