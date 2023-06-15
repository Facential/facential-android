package com.capestone.facential.ui.main

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.capestone.facential.data.remote.response.ClassifyResponse
import com.capestone.facential.data.remote.retrofit.ApiConfig
import com.capestone.facential.databinding.FragmentScanBinding
import com.capestone.facential.ui.camera.CameraActivity
import com.capestone.facential.ui.result.ResultActivity
import com.capestone.facential.utils.reduceFileImage
import com.capestone.facential.utils.rotateFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private var getFile: File? = null

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    companion object {
        const val CAMERA_X_RESULT = 200
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnSubmit.setOnClickListener {
            isLoading(true)
            uploadImage()
        }

        return view
    }

    private fun uploadImage() {
        if (getFile != null) {

            auth = FirebaseAuth.getInstance()
            currentUser = auth.currentUser

            val userId = currentUser?.uid.toString().toRequestBody("text/plain".toMediaType())

            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image", file.name, requestImageFile
            )
            ApiConfig.getApiService().postFace(
                userId,
                imageMultipart
            ).enqueue(object : Callback<ClassifyResponse> {
                override fun onResponse(
                    call: Call<ClassifyResponse>, response: Response<ClassifyResponse>
                ) {
                    if (response.isSuccessful) {
                        startActivity(Intent(requireContext(), ResultActivity::class.java))
                        isLoading(false)
                    } else {
                        Toast.makeText(
                            requireContext(), "Face not detected", Toast.LENGTH_SHORT
                        ).show()
                        isLoading(false)
                    }
                }

                override fun onFailure(call: Call<ClassifyResponse>, t: Throwable) {
                    Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                    isLoading(false)
                }

            })

        } else {
            Toast.makeText(
                requireContext(), "Take a picture first", Toast.LENGTH_SHORT
            ).show()
            isLoading(false)
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == ScanFragment.CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun isLoading(showLoading: Boolean) {
        if (showLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.linearLayout5.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.linearLayout5.visibility = View.VISIBLE
        }
    }

}