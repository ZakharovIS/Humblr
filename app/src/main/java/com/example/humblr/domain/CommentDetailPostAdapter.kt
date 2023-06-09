package com.example.humblr.domain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.humblr.data.models.Children
import com.example.humblr.databinding.ItemCommentBinding
import java.text.SimpleDateFormat
import java.util.*

class CommentDetailPostAdapter(
    private val values: List<Children.ChildrenComments>
) : RecyclerView.Adapter<CommentDetailPostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentDetailPostViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context))
        return CommentDetailPostViewHolder(binding)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: CommentDetailPostViewHolder, position: Int) {
        val item = values[position]
        holder.binding.author.text = item.data.author
        holder.binding.commentBody.text = item.data.body
        holder.binding.time.text = SimpleDateFormat(
            "dd-MM-yyyy hh:mm:ss", Locale.US
        ).format(item.data.created * 1000).toString()
        holder.binding.votes.text = item.data.score.toString()

        holder.binding.upvote.setOnClickListener {
            if(holder.binding.votes.text == item.data.score.toString()) {
                holder.binding.votes.text = (item.data.score + 1).toString()
                holder.binding.isUpvoted = true
            } else {
                holder.binding.votes.text = item.data.score.toString()
                holder.binding.isUpvoted = false
                holder.binding.isDownvoted = false
            }
        }
        holder.binding.downvote.setOnClickListener {
            if(holder.binding.votes.text == item.data.score.toString()) {
                holder.binding.votes.text = (item.data.score - 1).toString()
                holder.binding.isDownvoted = true
            } else {
                holder.binding.votes.text = item.data.score.toString()
                holder.binding.isUpvoted = false
                holder.binding.isDownvoted = false
            }

        }
    }
}


class CommentDetailPostViewHolder(val binding: ItemCommentBinding) :
    RecyclerView.ViewHolder(binding.root)