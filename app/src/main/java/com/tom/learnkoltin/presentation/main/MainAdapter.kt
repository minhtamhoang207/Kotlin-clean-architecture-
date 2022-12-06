package com.tom.learnkoltin.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tom.learnkoltin.databinding.MainItemBinding
import com.tom.learnkoltin.domain.model.Post

class MainAdapter (private val listPost:MutableList<Post>) : RecyclerView.Adapter<MainAdapter.ViewHolder>(){

    inner class ViewHolder(private val itemBinding: MainItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(post: Post){
            itemBinding.postBody.text = post.body
            itemBinding.postTitle.text = post.id.toString().plus(". ".plus(post.title))
            itemBinding.root.setOnClickListener {
//                onTapListener?.onTap(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPost[position])
    }

    override fun getItemCount(): Int {
        return listPost.size
    }

    fun updateList(mProducts: List<Post>){
        listPost.clear()
        listPost.addAll(mProducts)
        notifyDataSetChanged()
    }
}