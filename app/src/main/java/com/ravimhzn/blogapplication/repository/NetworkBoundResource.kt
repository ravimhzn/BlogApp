package com.ravimhzn.blogapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.codingwithmitch.mviexample.util.ApiEmptyResponse
import com.codingwithmitch.mviexample.util.ApiErrorResponse
import com.codingwithmitch.mviexample.util.ApiSuccessResponse
import com.codingwithmitch.mviexample.util.GenericApiResponse
import com.ravimhzn.blogapplication.util.Constants.Companion.TESTING_NETWORK_DELAY
import com.ravimhzn.blogapplication.util.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource<ResponseObject, ViewStateType> {

    protected val result = MediatorLiveData<Result<ViewStateType>>()

    init {
        result.value = Result.loading(true)
        GlobalScope.launch(IO) {
            delay(TESTING_NETWORK_DELAY)

            withContext(Main) {
                val apiResponse = createCall()
                result.addSource(apiResponse) { response ->
                    result.removeSource(apiResponse)
                    handleNetworkCall(response)
                }
            }
        }
    }

    private fun handleNetworkCall(response: GenericApiResponse<ResponseObject>?) {
        when (response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse -> {
                println("Debug -> NetworkBoundResource: ${response.errorMessage}")
                onReturnError(response.errorMessage)
            }
            is ApiEmptyResponse -> {
                println("Debug -> NetworkBoundResource: HTTP 204. Nothing was returned")
                onReturnError("HTTP 204. Nothing was returned")
            }
        }
    }

    fun onReturnError(message: String) {
        result.value = Result.error(message)
    }

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    abstract fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    fun asLiveData() = result as LiveData<Result<ViewStateType>>
}