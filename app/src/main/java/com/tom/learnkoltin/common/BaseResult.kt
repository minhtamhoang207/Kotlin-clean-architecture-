package com.tom.learnkoltin.common

/**
 * A generic class that holds a value or an exception
 */
sealed class BaseResult<out R> {
    data class Success<out T>(val data: T) : BaseResult<T>()
    data class Error(val exception: ApiException) : BaseResult<Nothing>()
}