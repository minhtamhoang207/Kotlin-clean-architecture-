package com.tom.learnkoltin.data.remote.main.repository

import com.tom.learnkoltin.common.ApiException
import com.tom.learnkoltin.domain.model.Post
import com.tom.learnkoltin.common.BaseResult
import com.tom.learnkoltin.data.remote.main.api.MainApi
import com.tom.learnkoltin.data.remote.main.dto.toPost
import com.tom.learnkoltin.domain.repository.IMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val mainApiService: MainApi
): IMainRepository {

    override suspend fun getPosts(): Flow<BaseResult<List<Post>>> {
        return flow {
            val response = mainApiService.getPosts()
            if(response.isSuccessful){
                val body = response.body()?.map { it.toPost() }
                emit(BaseResult.Success(body.orEmpty()))
            } else {
                emit(BaseResult.Error(ApiException(
                    response.code(),
                    response.message())
                ))
            }
        }
    }
}