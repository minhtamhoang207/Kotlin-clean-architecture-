package com.tom.learnkoltin.presentation.main

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tom.learnkoltin.R
import com.tom.learnkoltin.databinding.HomeItemBinding
import com.tom.learnkoltin.databinding.MainItemBinding
import com.tom.learnkoltin.domain.model.Post


class MainAdapter (
    private val onClickListener: OnClickListener
) : ListAdapter<Post, MainAdapter.ViewHolder>(ListDiffCallback()) {

    inner class ViewHolder(
        private val binding: HomeItemBinding,
        onItemClick: OnClickListener
    ) : RecyclerView.ViewHolder(binding.root){
        private var currentPost: Post? = null

        init {
            binding.root.setOnClickListener{
                currentPost?.let {
                    onItemClick.onClick(it)
                }
            }
            binding.root.setOnLongClickListener{
                currentPost?.let {
                    onItemClick.onLongPress(it)
                }
                true
            }
        }


        fun bind(post: Post){
            currentPost = post
//            binding.postBody.text = post.body
//            binding.postTitle.text = post.id.toString().plus(". ".plus(post.title))
//            Glide.with(binding.imgView)
//                .load("https://thumbs.dreamstime.com/b/pet-cat-green-cats-eyes-gray-big-102425920.jpg")
//                .fitCenter()
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .error(R.drawable.ic_baseline_refresh_24)
//                .into(binding.imgView);
//            itemBinding.root.setOnClickListener {
//                itemView.findNavController().navigate(R.id.detailFragment2)
//            }
//
//            itemBinding.root.setOnLongClickListener {
//                listPost.removeAt(index = position)
//                notifyItemRemoved(position)
//                notifyItemRangeChanged(position, listPost.size - 1)
//                true
//            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, onItemClick = onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class OnClickListener(
        val clickListener: (post: Post) -> Unit,
        val longPressListener: (post: Post) -> Unit
    ) {
        fun onClick(post: Post) = clickListener(post)
        fun onLongPress(post: Post) = longPressListener(post)
    }
}

private class ListDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}