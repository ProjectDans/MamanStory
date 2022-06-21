package com.danscoding.mamanstory.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danscoding.mamanstory.DetailSnapStoryActivity
import com.danscoding.mamanstory.ListStoryPost
import com.danscoding.mamanstory.databinding.ItemStoryBinding

class AdapterStory: RecyclerView.Adapter<AdapterStory.ViewHolderStory>() {

    private var storyList : List<ListStoryPost>? = null

    fun setStoryList(storyList: List<ListStoryPost>?){
        this.storyList = storyList
    }

    inner class ViewHolderStory(private val itemStoryBinding: ItemStoryBinding): RecyclerView.ViewHolder(itemStoryBinding.root) {
        fun bind(dataStoryAccount : ListStoryPost) {
            itemView.setOnClickListener {
                val itemIntent = Intent(itemView.context, DetailSnapStoryActivity::class.java)
                itemIntent.putExtra("storyData", dataStoryAccount)
                val optionsCompat : ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(itemStoryBinding.imageStory, "image"),
                    Pair(itemStoryBinding.userNameStory, "name"),
                    Pair(itemStoryBinding.textDescription, "description")
                )

                itemView.context.startActivity(itemIntent, optionsCompat.toBundle())
            }

            itemStoryBinding.apply {
                Glide.with(itemView)
                    .load(dataStoryAccount.photoUrl)
                    .centerCrop()
                    .into(imageStory)
                userNameStory.text = dataStoryAccount.name
                textDescription.text = dataStoryAccount.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStory {
        val storyView = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderStory(storyView)
    }

    override fun onBindViewHolder(holder: ViewHolderStory, position: Int) {
        holder.bind(storyList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        return if(storyList ==  null)0
        else storyList?.size!!
    }
}