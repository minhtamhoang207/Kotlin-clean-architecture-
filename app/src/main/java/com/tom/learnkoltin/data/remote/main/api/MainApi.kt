package com.tom.learnkoltin.data.remote.main.api

import com.tom.learnkoltin.data.remote.main.dto.PostDTO
import retrofit2.Response
import retrofit2.http.GET

interface MainApi {
    @GET("posts")
    suspend fun getPosts(): Response<List<PostDTO>>

//    suspend fun login(@Body loginRequest: LoginRequest) : Response<WrappedResponse<LoginResponse>>

}