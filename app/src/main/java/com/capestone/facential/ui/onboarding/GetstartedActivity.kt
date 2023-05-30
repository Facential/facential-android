package com.capestone.facential.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capestone.facential.data.local.SharedPreferencesManager
import com.capestone.facential.databinding.ActivityGetstartedBinding
import com.capestone.facential.ui.auth.LoginActivity

class GetstartedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetstartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGetstartedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreferencesManager = SharedPreferencesManager(this)

        binding.btnGetStarted.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            sharedPreferencesManager.setOnboardingFinished()
        }
    }
}