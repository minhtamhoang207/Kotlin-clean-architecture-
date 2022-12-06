package com.tom.learnkoltin.data.remote

import java.io.IOException

class ApiException(
    private val errorCode: Int,
    private val errorDetail: String = ""
) : IOException() {
    companion object {
        private const val TOKEN_HAS_EXPIRED = "The incoming token has expired"
        const val ERROR_CODE_NO_INTERNET = -1
    }

    val isTokenHasExpired: Boolean
        get() = errorCode == 401 && errorDetail == TOKEN_HAS_EXPIRED
}
