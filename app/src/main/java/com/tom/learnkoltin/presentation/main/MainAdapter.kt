package com.tom.learnkoltin.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tom.learnkoltin.R
import com.tom.learnkoltin.databinding.MainItemBinding
import com.tom.learnkoltin.domain.model.Post
import dagger.hilt.android.qualifiers.ApplicationContext

class MainAdapter (
    private val listPost:MutableList<Post>,
    private val context: Context
    ) : RecyclerView.Adapter<MainAdapter.ViewHolder>(){

    inner class ViewHolder(private val itemBinding: MainItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(post: Post, position: Int, context: Context){
            itemBinding.postBody.text = post.body
            itemBinding.postTitle.text = post.id.toString().plus(". ".plus(post.title))
            Glide.with(context)
                .load("https://i.pinimg.com/736x/4c/81/69/4c8169785415eff5e54e2196f9c7b5c2.jpg")
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_refresh_24)
                .error(R.drawable.ic_launcher_foreground)
                .into(itemBinding.imgView);
            if(position % 2 == 0){
                itemBinding.cardItem.setBackgroundResource(R.color.purple_200)
            }
            itemBinding.root.setOnClickListener {
                listPost.removeAt(index = position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, listPost.size - 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPost[position], position, context)
    }

    override fun getItemCount(): Int {
        return listPost.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(mProducts: List<Post>){
        listPost.clear()
        listPost.addAll(mProducts)
        notifyDataSetChanged()
    }
}