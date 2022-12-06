package com.tom.learnkoltin.presentation.main

import com.tom.learnkoltin.domain.model.Post
//
//data class MainState(
//    val isLoading: Boolean = false,
//    val listPost: List<Post> = emptyList(),
//    val error: String = "",
//)

sealed class MainState {
    object Init : MainState()
    data class IsLoading(val isLoading: Boolean) : MainState()
    data class ShowToast(val message : String) : MainState()
}
