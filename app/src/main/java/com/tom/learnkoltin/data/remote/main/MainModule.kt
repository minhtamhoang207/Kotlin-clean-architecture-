package com.tom.learnkoltin.data.remote.main

import com.tom.learnkoltin.data.remote.main.api.MainApi
import com.tom.learnkoltin.data.remote.main.repository.MainRepositoryImpl
import com.tom.learnkoltin.di.RemoteModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [RemoteModule::class])
@InstallIn(SingletonComponent::class)
class MainModule {

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit) : MainApi {
        return retrofit.create(MainApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(loginApi: MainApi) : MainRepositoryImpl {
        return MainRepositoryImpl(loginApi)
    }
}