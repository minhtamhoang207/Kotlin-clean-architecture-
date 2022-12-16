package com.tom.learnkoltin.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tom.learnkoltin.databinding.FragmentHomeBinding
import com.tom.learnkoltin.domain.model.Post
import com.tom.learnkoltin.presentation.main.MainAdapter
import com.tom.learnkoltin.presentation.main.MainState
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private var refresh : Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val mAdapter = MainAdapter(
            MainAdapter.OnClickListener(
                { post -> adapterOnClick(post) },
                { post -> adapterOnLongPress(post) }
            )
        )

        val navController = findNavController()

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("refresh")
            ?.observe(navController.currentBackStackEntry!!) {
                refresh = it
            }

        if(refresh){
            init()
        }
        setupRecyclerView(mAdapter)
        observeViewModel(mAdapter)

        return binding.root

    }

    private fun init(){
        println(Thread.currentThread().name)
        viewModel.getPost()
    }

    private fun setupRecyclerView(mAdapter: MainAdapter){
        binding.homeList.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun adapterOnClick(post: Post) {
        println(post.toString())
//        findNavController().navigate(R.id.detailFragment2)
    }

    private fun adapterOnLongPress(post: Post) {
        viewModel.deleteElement(post.id)
    }

    private fun observeViewModel(mAdapter: MainAdapter) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.mainState.collectLatest {
                handleState(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.mListPost.collectLatest {
                mAdapter.submitList(it as MutableList<Post>)
            }
        }
    }

    private fun handleState(state: MainState){
        when(state){
            is MainState.IsLoading -> handleLoading(state.isLoading)
            is MainState.ShowToast -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            is MainState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            binding.homeListProgressbar.visibility = View.VISIBLE
            binding.homeList.visibility = View.GONE
        } else{
            binding.homeListProgressbar.visibility = View.GONE
            binding.homeList.visibility = View.VISIBLE

        }
    }
}