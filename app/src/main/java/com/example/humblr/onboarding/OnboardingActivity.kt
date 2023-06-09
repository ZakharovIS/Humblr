package com.example.humblr.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.humblr.R
import com.example.humblr.auth.AuthorizationActivity
import com.example.humblr.databinding.ActivityOnboardingBinding


class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter
    private lateinit var indicatorsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnboardingItems()
        setupIndicators()
        setCurrentIndicator(0)
        binding.textSkip.setOnClickListener {
            val intent = Intent(this, AuthorizationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setOnboardingItems() {
        onboardingAdapter = OnboardingAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.onboarding_image_1,
                    title = getString(R.string.onboarding_title_1),
                    description = getString(R.string.onboarding_description_1)
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.onboarding_image_2,
                    title = getString(R.string.onboarding_title_2),
                    description = getString(R.string.onboarding_description_2)
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.onboarding_image_3,
                    title = getString(R.string.onboarding_title_3),
                    description = getString(R.string.onboarding_description_3)
                )
            )
        )
        val onboardingViewPager = binding.onboardingViewPager
        onboardingViewPager.adapter = onboardingAdapter
        onboardingViewPager.registerOnPageChangeCallback(object:
        ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                if (position == 2) {
                    binding.textSkip.text = getString(R.string.onboarding_done)
                } else {
                    binding.textSkip.text = getString(R.string.skip)
                }
            }
        })
        (onboardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setupIndicators() {
        indicatorsContainer = binding.indicatorsContainer
        val indicators = arrayOfNulls<ImageView>(onboardingAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for(i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorsContainer.childCount
        for(i in 0 until childCount) {
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if(i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active_background
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
            }
        }
    }
}