package com.tom.learnkoltin.domain.usecase

import com.tom.learnkoltin.data.repository.WeatherRepository
import com.tom.learnkoltin.domain.model.Post
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import com.tom.learnkoltin.common.Result
import com.tom.learnkoltin.common.successOr
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(private val repository: WeatherRepository){
    suspend fun invoke(): Flow<Result<List<Post>>> {
        return repository.getPosts()
    }
}
