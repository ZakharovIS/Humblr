package com.example.humblr.domain

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.humblr.data.models.Children
import com.example.humblr.databinding.ItemSubredditBinding

class SubredditsAdapter(
    private val onClickSubscribe: (String, Boolean) -> Unit
) :
    PagingDataAdapter<Children.ChildrenSubreddits, SubredditsViewHolder>(diffCallback = SubredditsDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditsViewHolder {
        return SubredditsViewHolder(
            ItemSubredditBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SubredditsViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.nameSubreddit.text = item!!.data.display_name_prefixed
        holder.binding.titleSubreddit.text = item.data.title
        holder.binding.descriptionSubreddit.text = item.data.public_description
        holder.binding.isSubscribed = item.data.user_is_subscriber

        holder.binding.share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://www.reddit.com/" + item.data.display_name_prefixed
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            holder.binding.root.context.startActivity(shareIntent)
        }

        holder.binding.subscribeButton.setOnClickListener {
            onClickSubscribe(item.data.name, holder.binding.isSubscribed == true)
            holder.binding.isSubscribed = holder.binding.isSubscribed == false
        }

    }
}

class SubredditsDiffUtilCallback : DiffUtil.ItemCallback<Children.ChildrenSubreddits>() {
    override fun areItemsTheSame(
        oldItem: Children.ChildrenSubreddits,
        newItem: Children.ChildrenSubreddits
    ): Boolean =
        oldItem.data.name == newItem.data.name

    override fun areContentsTheSame(
        oldItem: Children.ChildrenSubreddits,
        newItem: Children.ChildrenSubreddits
    ): Boolean =
        oldItem == newItem


}

class SubredditsViewHolder(val binding: ItemSubredditBinding) :
    RecyclerView.ViewHolder(binding.root) {
}