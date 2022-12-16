package com.tom.learnkoltin.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tom.learnkoltin.common.BaseResult
import com.tom.learnkoltin.domain.model.Post
import com.tom.learnkoltin.domain.usecase.GetPostsUseCase
import com.tom.learnkoltin.presentation.main.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel(){

    private val viewModelState = MutableStateFlow<MainState>(MainState.Init)
    val mainState: StateFlow<MainState> get() = viewModelState

    private val listPost = MutableStateFlow<List<Post>>(mutableListOf())
    val mListPost: StateFlow<List<Post>> get() = listPost

    fun getPost(){
        setLoading()
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                when(val response = getPostsUseCase.invoke()){
                    is BaseResult.Success -> {
                        listPost.value = response.data
                        hideLoading()
                    }
                    is BaseResult.Error -> {
                        onError(message = response.exception.message.orEmpty())
                    }
                }
            } catch (e: Exception){
                onError(message = e.message.orEmpty())
            }
        }
    }

    fun deleteElement(postID: Int){
        listPost.value = listPost.value.filter { it.id != postID}
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println(throwable.localizedMessage.orEmpty())
    }

    private fun onError(message: String) {
        hideLoading()
        showToast(message)
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
}