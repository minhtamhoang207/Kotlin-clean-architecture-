package com.tom.learnkoltin.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tom.learnkoltin.common.BaseResult
import com.tom.learnkoltin.domain.model.Post
import com.tom.learnkoltin.domain.usecase.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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
        setLoading()
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = getPostsUseCase.invoke()
            withContext(Dispatchers.Main) {
                when(response){
                    is BaseResult.Success -> {
                        listPost.value = response.data
                        hideLoading()
                    }
                    is BaseResult.Error -> {
                        onError(message = response.exception.message.orEmpty())
                    }
                }
            }
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable.localizedMessage as String)
    }

    private fun onError(message: String) {
        hideLoading()
        showToast(message)
    }

//    fun getPost(){
//        viewModelScope.launch {
//            getPostsUseCase.invoke()
//                .onStart {
//                    setLoading()
//                }
//                .catch { exception ->
//                    hideLoading()
//                    showToast(exception.message.toString())
//                }
//                .collect { baseResult ->
//                    hideLoading()
//                    when(baseResult){
//                        is BaseResult.Success -> {
//                            listPost.value = baseResult.data
//                        }
//                        is BaseResult.Error -> {
//                            showToast(message = baseResult.exception.message.orEmpty())
//                        }
//                    }
//                }
//        }
//    }

    private fun setLoading(){
        viewModelState.value = MainState.IsLoading(true)
    }

    private fun hideLoading(){
        viewModelState.value = MainState.IsLoading(false)
    }

    private fun showToast(message: String){
        viewModelState.value = MainState.ShowToast(message)
    }
}