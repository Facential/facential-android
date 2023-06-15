package com.capestone.facential.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.capestone.facential.R
import com.capestone.facential.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingItemAdapter: OnboardingItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setOnboardingItem()
        setupIndicators()
        setCurrentIndicator(0)

    }

    private fun setOnboardingItem() {
        onboardingItemAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.onboarding_welcome,
                    title = getString(R.string.title1),
                    description = getString(R.string.description1)
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.onboarding_1,
                    title = getString(R.string.title2),
                    description = getString(R.string.description2)
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.onboarding_2,
                    title = getString(R.string.title3),
                    description = getString(R.string.description3)
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.onboarding_3,
                    title = getString(R.string.title4),
                    description = getString(R.string.description4)
                ),
            )
        )

        binding.vpOnboardingMain.adapter = onboardingItemAdapter
        binding.vpOnboardingMain.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (binding.vpOnboardingMain.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        binding.ivNext.setOnClickListener {
            if (binding.vpOnboardingMain.currentItem + 1 < onboardingItemAdapter.itemCount) {
                binding.vpOnboardingMain.currentItem += 1
            } else {
                startActivity(Intent(this, GetstartedActivity::class.java))
                finish()
            }
        }
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(onboardingItemAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                binding.cIndicator.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = binding.cIndicator.childCount
        for (i in 0 until childCount) {
            val imageView = binding.cIndicator.getChildAt(i) as ImageView
            if (i == position) {
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