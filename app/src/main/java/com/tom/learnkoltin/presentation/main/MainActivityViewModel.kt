package com.tom.learnkoltin.presentation.main

import com.tom.learnkoltin.common.Result
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tom.learnkoltin.domain.model.Post
import com.tom.learnkoltin.domain.usecase.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel(){


    private val viewModelState = MutableStateFlow<MainState>(MainState.Init)
    val mainState: StateFlow<MainState> get() = viewModelState

    private val listPost = MutableStateFlow<List<Post>>(mutableListOf())
    val mListPost: StateFlow<List<Post>> get() = listPost

    fun getPost(){
        viewModelScope.launch {
            getPostsUseCase.invoke()
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect { baseResult ->
                    hideLoading()
                    when(baseResult){
                        is Result.Success -> {
                            listPost.value = baseResult.data
                        }
                        is Result.Error -> {
                            showToast(baseResult.exception.message.toString())
                        }
                    }
                }
        }
    }

    private fun setLoading(){
        viewModelState.value = MainState.IsLoading(true)
    }

    private fun hideLoading(){
        viewModelState.value = MainState.IsLoading(false)
    }

    private fun showToast(message: String){
        viewModelState.value = MainState.ShowToast(message)
    }


//    private suspend fun getPost(){
//        getPostsUseCase().onEach { result ->
//            when (result) {
//                is Result.Success -> {
//                    viewModelState.update {it.copy(listPost = result.data)}
//                }
//                is Result.Error -> {
//                    viewModelState.update {it.copy(error = result.toString())}
//                }
//            }
//        }.launchIn(viewModelScope)
//    }
}