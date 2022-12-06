package com.tom.learnkoltin.di

import com.tom.learnkoltin.common.Constants
import com.tom.learnkoltin.common.ErrorResponseInterceptor
import com.tom.learnkoltin.data.local.share_pref.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient) : Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            client(okHttp)
            baseUrl(Constants.BASE_URL)
        }.build()
    }

    @Singleton
    @Provides
    fun provideOkHttp(
        requestInterceptor: Interceptor,
        errorResponseInterceptor: ErrorResponseInterceptor
    ) : OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)
            addInterceptor(errorResponseInterceptor)
        }.build()
    }


    @Singleton
    @Provides
    fun provideApiInterceptor(sharedPrefs: SharedPrefs): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val token = sharedPrefs.getToken()
            val newRequest = request.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("charset", "utf-8")
                .addHeader("Authorization", "Bear $token")
                .method(request.method, request.body)
            chain.proceed(newRequest.build())
        }
}