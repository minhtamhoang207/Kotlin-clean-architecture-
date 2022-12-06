package com.tom.learnkoltin.domain.usecase

import com.tom.learnkoltin.common.BaseResult
import com.tom.learnkoltin.domain.model.Post
import kotlinx.coroutines.flow.Flow
import com.tom.learnkoltin.data.remote.main.repository.MainRepositoryImpl
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(private val repository: MainRepositoryImpl){
    suspend fun invoke(): Flow<BaseResult<List<Post>>> {
        return repository.getPosts()
    }
}
