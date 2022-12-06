package com.tom.learnkoltin.domain.repository

import com.tom.learnkoltin.domain.model.Post
import com.tom.learnkoltin.common.Result
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun getPosts(): Flow<Result<List<Post>>>
}