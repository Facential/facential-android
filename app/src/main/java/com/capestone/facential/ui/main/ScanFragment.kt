package com.capestone.facential.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capestone.facential.databinding.FragmentScanBinding
import com.capestone.facential.ui.camera.CameraActivity
import com.capestone.facential.ui.camera.ConfirmActivity

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnToCamera.setOnClickListener {
            val intent = Intent(requireContext(), ConfirmActivity::class.java)
            startActivity(intent)
        }

        return view
    }


}