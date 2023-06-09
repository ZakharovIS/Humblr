package com.example.humblr.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.humblr.databinding.OnboardItemContainerBinding

class OnboardingAdapter(
    private val onboardingItems: List<OnboardingItem> = emptyList()
    ) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(
            OnboardItemContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = onboardingItems[position]
        holder.binding.imageOnboarding.setImageResource(item.onboardingImage)
        holder.binding.textTitle.text = item.title
        holder.binding.textDescription.text = item.description
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }

    class OnboardingViewHolder(val binding: OnboardItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root)

}