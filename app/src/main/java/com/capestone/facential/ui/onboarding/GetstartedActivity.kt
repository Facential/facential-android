package com.capestone.facential.ui.onboarding

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION

        val permissions = arrayOf(locationPermission, coarseLocationPermission)
        val requestCode = LOCATION_PERMISSION_REQUEST_CODE

        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, start the next activity
                onBoardingFinished()
            } else {
                // Location permission denied, show a message or handle accordingly
                // For example, you can display a Toast message
                onBoardingFinished()
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onBoardingFinished() {
        val sharedPreferencesManager = SharedPreferencesManager(this)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        sharedPreferencesManager.setOnboardingFinished()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }
}