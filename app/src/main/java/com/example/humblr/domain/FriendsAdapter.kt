package com.example.humblr.domain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.humblr.R
import com.example.humblr.data.models.UserData
import com.example.humblr.databinding.ItemFriendBinding

class FriendsAdapter(
    private val values: List<UserData>
) : RecyclerView.Adapter<FriendsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context))
        return FriendsViewHolder(binding)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val item = values[position]
        holder.binding.name.text = item.name
        Glide.with(holder.binding.root.context)
            .load(item.snoovatar_img)
            .placeholder(R.drawable.avatar_default_1)
            .into(holder.binding.avatarPicture)
    }
}


class FriendsViewHolder(val binding: ItemFriendBinding) :
    RecyclerView.ViewHolder(binding.root)