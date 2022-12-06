package com.tom.learnkoltin.domain.repository

import com.tom.learnkoltin.common.BaseResult
import com.tom.learnkoltin.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface IMainRepository {
    suspend fun getPosts(): Flow<BaseResult<List<Post>>>
}