package com.capestone.facential.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capestone.facential.R
import com.capestone.facential.databinding.FragmentProfileBinding
import com.capestone.facential.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        currentUser?.let { user ->
            val profilePictureUrl = user.photoUrl.toString()
            val defaultImageUrl = R.drawable.ic_profile

            if (profilePictureUrl.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(profilePictureUrl)
                    .apply(RequestOptions())
                    .error(defaultImageUrl)
                    .into(binding.ivProfile)
            } else {
                Glide.with(requireContext())
                    .load(defaultImageUrl)
                    .into(binding.ivProfile)
            }

            val fullName = user.displayName
            val email = user.email

            binding.tvName.text = fullName
            binding.tvEmail.text = email
        }

        binding.btnLogout.setOnClickListener {
            signOut()
        }

        return view
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
        requireActivity().finish()
    }
}