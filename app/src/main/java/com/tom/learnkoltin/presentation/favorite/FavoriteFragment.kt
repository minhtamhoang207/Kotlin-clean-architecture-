package com.tom.learnkoltin.presentation.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tom.learnkoltin.R
import com.tom.learnkoltin.databinding.FragmentFavoriteBinding
import com.tom.learnkoltin.databinding.FragmentListBinding
import com.tom.learnkoltin.domain.model.Job
import com.tom.learnkoltin.domain.model.Post
import com.tom.learnkoltin.presentation.main.MainAdapter

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val favAdapter = FavoriteAdapter(
           FavoriteAdapter.OnClickListener(
               { job -> adapterOnClick(job) },
               { job -> adapterOnLongPress(job) }
           )
        )

        setupRecyclerView(favAdapter)
        return binding.root
    }

    private fun setupRecyclerView(favAdapter: FavoriteAdapter){

        val listJob = mutableListOf<Job>()

        listJob.add(Job(1,"", 2))
        listJob.add(Job(2,"", 3))
        listJob.add(Job(3,"", 4))
        listJob.add(Job(4,"", 5))
        listJob.add(Job(5,"", 6))
        listJob.add(Job(6,"", 7))
        listJob.add(Job(7,"", 8))


        binding.favoriteList.apply {
            adapter = favAdapter
            layoutManager = LinearLayoutManager(context)
        }
        favAdapter.submitList(listJob)
    }

    private fun adapterOnClick(job: Job) {
        println(job.toString())
    }

    private fun adapterOnLongPress(post: Job) {

    }
}