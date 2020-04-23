package com.ravimhzn.blogapplication.network

import androidx.lifecycle.LiveData
import com.codingwithmitch.mviexample.util.GenericApiResponse
import com.ravimhzn.blogapplication.model.BlogPost
import com.ravimhzn.blogapplication.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/placeholder/blogs")
    fun getBlogPosts(): LiveData<GenericApiResponse<List<BlogPost>>>

    @GET("/placeholder/user/{userId}")
    fun getUser(@Path("userId") userId: String): LiveData<GenericApiResponse<User>>
}