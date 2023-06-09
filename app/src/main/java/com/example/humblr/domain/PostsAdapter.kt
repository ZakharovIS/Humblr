package com.example.humblr.domain

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.humblr.data.models.Children
import com.example.humblr.databinding.ItemPostBinding

class PostsAdapter(
    private val onClick: (String) -> Unit,
    private val onClickSubreddit: (String) -> Unit,
    private val onClickUser: (String) -> Unit,
) :
    PagingDataAdapter<Children.ChildrenPost, PostsViewHolder>(diffCallback = DiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val item = getItem(position)
        var isExpanded = false
        Glide.with(holder.binding.root.context).clear(holder.binding.imageSubreddit)
        holder.binding.imageSubreddit.visibility = View.GONE
        holder.binding.author.visibility = View.GONE
        holder.binding.commentsCount.visibility = View.GONE
        holder.binding.descriptionText.visibility = View.GONE
        holder.binding.commentsIcon.visibility = View.GONE
        holder.binding.link.visibility = View.GONE
        if (item?.data?.post_hint == "image") {
            Glide.with(holder.binding.root.context)
                .load(item.data.url)
                .into(holder.binding.imageSubreddit)
        }
        if (item?.data?.post_hint == "link") {
            holder.binding.link.text = item.data.url_overridden_by_dest
        }
        holder.binding.titleSubreddit.text = item?.data?.title
        holder.binding.author.text = item?.data?.author
        holder.binding.commentsCount.text = item?.data?.num_comments.toString()
        holder.binding.descriptionText.text = item?.data?.selftext
        holder.binding.nameSubreddit.text = item?.data?.subreddit_name_prefixed
        holder.binding.root.setOnClickListener {
            if (!isExpanded) {
                if (holder.binding.imageSubreddit.drawable != null) {
                    holder.binding.imageSubreddit.visibility = View.VISIBLE
                }
                if (holder.binding.descriptionText.text != "") {
                    holder.binding.descriptionText.visibility = View.VISIBLE
                }
                if (holder.binding.link.text != null) {
                    holder.binding.link.visibility = View.VISIBLE
                }
                holder.binding.author.visibility = View.VISIBLE
                holder.binding.commentsCount.visibility = View.VISIBLE
                holder.binding.commentsIcon.visibility = View.VISIBLE
                isExpanded = true
            } else {
                if (item != null) onClick(item.data.permalink)
            }
        }
        holder.binding.author.setOnClickListener {
            if (item != null) {
                onClickUser(item.data.author)
            }
        }
        holder.binding.nameSubreddit.setOnClickListener {
            if (item != null) {
                onClickSubreddit(item.data.subreddit_name_prefixed)
            }
        }

    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<Children.ChildrenPost>() {
    override fun areItemsTheSame(
        oldItem: Children.ChildrenPost,
        newItem: Children.ChildrenPost
    ): Boolean =
        oldItem.data.id == newItem.data.id

    override fun areContentsTheSame(
        oldItem: Children.ChildrenPost,
        newItem: Children.ChildrenPost
    ): Boolean =
        oldItem == newItem


}

class PostsViewHolder(val binding: ItemPostBinding) :
    RecyclerView.ViewHolder(binding.root)
