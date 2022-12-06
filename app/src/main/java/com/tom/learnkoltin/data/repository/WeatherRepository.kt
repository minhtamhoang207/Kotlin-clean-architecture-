package com.tom.learnkoltin.data.repository

import com.tom.learnkoltin.domain.model.Post
import com.tom.learnkoltin.domain.repository.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.tom.learnkoltin.common.Result
import com.tom.learnkoltin.data.remote.ApiService
import com.tom.learnkoltin.data.remote.dto.toPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: ApiService
): IWeatherRepository {

    override suspend fun getPosts(): Flow<Result<List<Post>>> {
        return flow {
            val posts: List<Post> = api.getPosts().map { it.toPost() }
            if (posts.isEmpty()) {
                emit(Result.Error(IllegalArgumentException("Posts not found")))
            } else {
                emit(Result.Success(posts))
            }
        }
    }
}