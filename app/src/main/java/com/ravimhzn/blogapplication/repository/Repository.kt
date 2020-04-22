package com.ravimhzn.blogapplication.repository

import androidx.lifecycle.LiveData
import com.codingwithmitch.mviexample.util.ApiSuccessResponse
import com.codingwithmitch.mviexample.util.GenericApiResponse
import com.ravimhzn.blogapplication.model.BlogPost
import com.ravimhzn.blogapplication.model.User
import com.ravimhzn.blogapplication.network.RetrofitBuilder
import com.ravimhzn.blogapplication.ui.main.state.MainViewState
import com.ravimhzn.blogapplication.util.Result

object Repository {

    fun getBlogPosts(): LiveData<Result<MainViewState>> {
        return object : NetworkBoundResource<List<BlogPost>, MainViewState>() {
            override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                return RetrofitBuilder.apiService.getBlogPosts()
            }

            override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
                result.value = Result.success(
                    data = MainViewState(blogPosts = response.body)
                )
            }
        }.asLiveData()
    }

    fun getUser(userId: String): LiveData<Result<MainViewState>> {
        return object : NetworkBoundResource<User, MainViewState>() {
            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return RetrofitBuilder.apiService.getUser(userId)
            }

            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = Result.success(
                    data = MainViewState(user = response.body)
                )
            }

        }.asLiveData()
    }

    //    fun getBlogPosts(): LiveData<DataState<MainViewState>> {
//        return Transformations
//            .switchMap(RetrofitBuilder.apiService.getBlogPosts()) { apiResponse ->
//                object : LiveData<DataState<MainViewState>>() {
//                    override fun onActive() {
//                        super.onActive()
//                        value = when (apiResponse) {
//                            is ApiSuccessResponse -> {
//                                DataState.success(
//                                    data = MainViewState(
//                                        blogPosts = apiResponse.body
//                                    )
//                                )
//                            }
//                            is ApiErrorResponse -> {
//                                DataState.error(
//                                    message = apiResponse.errorMessage
//                                )
//                            }
//                            is ApiEmptyResponse -> {
//                                DataState.error(
//                                    message = "HTTP 204 -> Nothing was returned"
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//    }

}