package com.tom.learnkoltin.data.remote

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.Interceptor
import okhttp3.Response

class ErrorResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.isSuccessful.not()) {
            val errorBody = response.body?.string()
            if (errorBody != null && errorBody.isNotEmpty()) {
                try {
                    throw ApiException(response.code, response.message)
                } catch (e: JsonSyntaxException) {
                    throw ApiException(response.code)
                }
            } else {
                throw ApiException(response.code)
            }
        }
        return response
    }
}