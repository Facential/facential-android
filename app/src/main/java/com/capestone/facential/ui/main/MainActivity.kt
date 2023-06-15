package com.capestone.facential.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.capestone.facential.R
import com.capestone.facential.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setCurrentFragment(HomeFragment())
        binding.bottomNavbar.selectedItemId = R.id.navigation_home

        binding.bottomNavbar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> setCurrentFragment(HomeFragment())
                R.id.navigation_scan -> setCurrentFragment(ScanFragment())
                R.id.navigation_profile -> setCurrentFragment(ProfileFragment())
            }
            true
        }
    }

    fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragment)
            commit()
        }

    fun navigateToProfileFragment() {
        setCurrentFragment(ProfileFragment())
        binding.bottomNavbar.selectedItemId = R.id.navigation_profile
    }
}