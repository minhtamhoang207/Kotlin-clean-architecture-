package com.tom.learnkoltin.presentation.main


sealed class MainState {
    object Init : MainState()
    data class IsLoading(val isLoading: Boolean) : MainState()
    data class ShowToast(val message : String) : MainState()
}
