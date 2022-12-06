package com.tom.learnkoltin.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tom.learnkoltin.databinding.ActivityMainBinding
import com.tom.learnkoltin.domain.model.Post
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //View binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        observe()
        viewModel.getPost()

    }

    private fun setupRecyclerView(){
        val mAdapter = MainAdapter(mutableListOf())

        binding.mainList.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observe(){
        observeState()
        observeProducts()
    }


    private fun observeState(){
        viewModel.mainState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(lifecycle.coroutineScope)
    }

    private fun observeProducts(){
        viewModel.mListPost
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { result ->
                handleProducts(result)
            }
            .launchIn(lifecycle.coroutineScope)
    }


    private fun handleState(state: MainState){
        when(state){
            is MainState.IsLoading -> handleLoading(state.isLoading)
            is MainState.ShowToast -> Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_SHORT).show()
            is MainState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            binding.loadingProgressBar.visibility = View.VISIBLE
        }else{
            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    private fun handleProducts(listPost: List<Post>){
        binding.mainList.adapter?.let {
            if(it is MainAdapter){
                it.updateList(listPost)
            }
        }
    }

}