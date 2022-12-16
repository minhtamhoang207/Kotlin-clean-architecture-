package com.tom.learnkoltin.presentation.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tom.learnkoltin.databinding.FavoriteItemBinding
import com.tom.learnkoltin.domain.model.Job

class FavoriteAdapter (
    private val onClickListener: OnClickListener
) : ListAdapter<Job, FavoriteAdapter.ViewHolder>(ListDiffCallback()) {

    inner class ViewHolder(
        private val binding: FavoriteItemBinding,
        onItemClick: OnClickListener
    ) : RecyclerView.ViewHolder(binding.root){
        private var currentJob: Job? = null

        init {
            binding.root.setOnClickListener{
                currentJob?.let {
                    onItemClick.onClick(it)
                }
            }
            binding.root.setOnLongClickListener{
                currentJob?.let {
                    onItemClick.onLongPress(it)
                }
                true
            }
        }


        fun bind(Job: Job){
            currentJob = Job
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, onItemClick = onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
    }

    class OnClickListener(
        val clickListener: (Job: Job) -> Unit,
        val longPressListener: (Job: Job) -> Unit
    ) {
        fun onClick(Job: Job) = clickListener(Job)
        fun onLongPress(Job: Job) = longPressListener(Job)
    }
}

private class ListDiffCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}