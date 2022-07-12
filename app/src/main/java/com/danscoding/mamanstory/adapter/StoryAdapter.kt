package com.danscoding.mamanstory.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danscoding.mamanstory.DetailSnapStoryActivity
import com.danscoding.mamanstory.DetailSnapStoryActivity.Companion.EXTRA_DETAIL
import com.danscoding.mamanstory.data.SnapStoryResponseItem
import com.danscoding.mamanstory.databinding.ItemStoryBinding

class StoryAdapter : PagingDataAdapter<SnapStoryResponseItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: SnapStoryResponseItem) {
            binding.apply {
                Glide.with(context)
                    .load(story.photoUrl)
                    .into(imageStory)
                userNameStory.text = story.name
                textDescription.text = story.description

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(imageStory, "image"),
                            Pair(userNameStory, "username"),
                            Pair(textDescription, "description")
                        )

                    Intent(context,DetailSnapStoryActivity::class.java).also {
                        it.putExtra(EXTRA_DETAIL, story)
                        context.startActivity(it, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holder.itemView.context, getItem(position)!!)
    }

    companion object{
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<SnapStoryResponseItem> =
            object: DiffUtil.ItemCallback<SnapStoryResponseItem>() {
                override fun areItemsTheSame(
                    oldItem: SnapStoryResponseItem,
                    newItem: SnapStoryResponseItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: SnapStoryResponseItem,
                    newItem: SnapStoryResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

}