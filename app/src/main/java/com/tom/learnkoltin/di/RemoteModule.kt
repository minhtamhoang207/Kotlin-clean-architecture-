package com.tom.learnkoltin.di

import com.tom.learnkoltin.common.Constants
import com.tom.learnkoltin.data.remote.ApiService
import com.tom.learnkoltin.data.remote.ErrorResponseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(
//        cache: Cache,
        @Named("interceptor") interceptor: Interceptor,
//        sslSocketFactory: SSLSocketFactory,
//        trustAllCerts: X509TrustManager,
    ): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
//        httpClientBuilder.cache(cache)
        httpClientBuilder.addInterceptor(interceptor)
        httpClientBuilder.addInterceptor(ErrorResponseInterceptor())

        httpClientBuilder.readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
//        httpClientBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts)
        httpClientBuilder.hostnameVerifier { _, _ -> true }

        return httpClientBuilder.build()
    }

    @Singleton
    @Provides
    @Named("interceptor")
//    fun provideApiInterceptor(appPrefs: AppPrefs): Interceptor =
    fun provideApiInterceptor(): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("charset", "utf-8")
                .method(request.method, request.body)
            chain.proceed(newRequest.build())
        }


    @Singleton
    @Provides
    fun provideApi(okHttpClient: OkHttpClient): ApiService =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)


}