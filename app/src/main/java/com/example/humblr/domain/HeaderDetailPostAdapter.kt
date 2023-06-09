package com.example.humblr.domain

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.humblr.data.models.Children
import com.example.humblr.databinding.HeaderDetailPostBinding
import java.text.SimpleDateFormat
import java.util.*

class HeaderDetailPostAdapter(
    private val value: Children.ChildrenPost,
    private val onClickUser: (String) -> Unit,
    private val onClickDownload: (String, Boolean) -> Unit
) : RecyclerView.Adapter<HeaderDetailPostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderDetailPostViewHolder {
        val binding = HeaderDetailPostBinding.inflate(LayoutInflater.from(parent.context))
        return HeaderDetailPostViewHolder(binding)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: HeaderDetailPostViewHolder, position: Int) {
        holder.binding.author.text = value.data.author
        holder.binding.published.text = SimpleDateFormat(
            "dd-MM-yyyy hh:mm:ss", Locale.US
        ).format(value.data.created * 1000).toString()

        if (value?.data?.post_hint == "image") {
            Log.d("picture", "${value.data.url}")
            Glide.with(holder.binding.root.context)
                .load(value.data.url)
                .into(holder.binding.picture)
        }
        if (value?.data?.post_hint == "link") {
            holder.binding.link.text = value.data.url_overridden_by_dest
        }
        holder.binding.votes.text = value.data.score.toString()
        holder.binding.titlePost.text = value.data.title
        holder.binding.author.text = value?.data?.author
        holder.binding.description.text = value?.data?.selftext
        holder.binding.author.setOnClickListener {
            onClickUser(value.data.author)
        }
        holder.binding.isDownloaded = value.data.saved
        holder.binding.download.setOnClickListener {
            onClickDownload(value.data.name, holder.binding.isDownloaded == true)
            holder.binding.isDownloaded = holder.binding.isDownloaded == false
        }
        holder.binding.upvote.setOnClickListener {
            if(holder.binding.votes.text == value.data.score.toString()) {
                holder.binding.votes.text = (value.data.score + 1).toString()
                holder.binding.isUpvoted = true
            } else {
                holder.binding.votes.text = value.data.score.toString()
                holder.binding.isUpvoted = false
                holder.binding.isDownvoted = false
            }
        }
        holder.binding.downvote.setOnClickListener {
            if(holder.binding.votes.text == value.data.score.toString()) {
                holder.binding.votes.text = (value.data.score - 1).toString()
                holder.binding.isDownvoted = true
            } else {
                holder.binding.votes.text = value.data.score.toString()
                holder.binding.isUpvoted = false
                holder.binding.isDownvoted = false
            }

        }

        holder.binding.share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://www.reddit.com" + value.data.permalink
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            holder.binding.root.context.startActivity(shareIntent)
        }
    }


}

class HeaderDetailPostViewHolder(val binding: HeaderDetailPostBinding) :
    RecyclerView.ViewHolder(binding.root)
