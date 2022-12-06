package com.tom.learnkoltin.data.remote

import com.tom.learnkoltin.data.remote.dto.PostDTO
import com.tom.learnkoltin.domain.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<PostDTO>

}