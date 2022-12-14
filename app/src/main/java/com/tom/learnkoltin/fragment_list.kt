package com.tom.learnkoltin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tom.learnkoltin.databinding.FragmentListBinding
import com.tom.learnkoltin.domain.model.Post
import com.tom.learnkoltin.presentation.main.MainActivityViewModel
import com.tom.learnkoltin.presentation.main.MainAdapter
import com.tom.learnkoltin.presentation.main.MainState
import kotlinx.coroutines.flow.collectLatest
import java.text.FieldPosition

class fragment_list : Fragment(){

    private lateinit var binding: FragmentListBinding
    private val viewModel: MainActivityViewModel by activityViewModels()
    private var refresh : Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentListBinding.inflate(inflater, container, false)

        val mAdapter = MainAdapter(
            MainAdapter.OnClickListener(
                {adapterOnClick(it )},
                {adapterOnLongPress(it )}
            )
        )

//        adapter = MemesAdapter(MemesAdapter.OnClickListener { photo ->
//            Toast.makeText(applicationContext, "${photo.name}", Toast.LENGTH_SHORT).show() })


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

        binding.fab.setOnClickListener{
            init()
        }

        return binding.root

    }

    private fun init(){
        println(Thread.currentThread().name)
        viewModel.getPost()
    }

    private fun setupRecyclerView(mAdapter: MainAdapter){
        binding.mainList.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun adapterOnClick(post: Post) {
        println(post.toString())
        findNavController().navigate(R.id.detailFragment2)
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
//
//        viewModel.mainState
//            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//            .onEach { state ->
//                handleState(state)
//            }
//            .launchIn(lifecycle.coroutineScope)

//        viewModel.mListPost
//            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//            .onEach { result ->
//                handleProducts(result)
//            }
//            .launchIn(lifecycle.coroutineScope)
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
            binding.loadingProgressBar.visibility = View.VISIBLE
            binding.mainList.visibility = View.GONE
        } else{
            binding.loadingProgressBar.visibility = View.GONE
            binding.mainList.visibility = View.VISIBLE

        }
    }

//    private fun handleProducts(listPost: List<Post>){
//        mAdapter.submitList(listPost as MutableList<Post>)
////        binding.mainList.adapter?.let { it ->
////            if(it is MainAdapter){
////                it.updateList(listPost)
////            }
////        }
//    }
}