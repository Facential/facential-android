package com.capestone.facential.ui.result

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.capestone.facential.data.remote.response.ResultResponse
import com.capestone.facential.data.remote.retrofit.ApiConfig
import com.capestone.facential.databinding.ActivityResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        val userId = currentUser?.uid.toString()

        ApiConfig.getApiService().getResult(
            userId
        ).enqueue(object : Callback<ResultResponse> {
            override fun onResponse(
                call: Call<ResultResponse>,
                response: Response<ResultResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    binding.tvSkinType.text = result.skinType
                    binding.tvSkinDesc.text = result.overall

                    val imageUrl = result.imageUrl

                    currentUser?.let {
                        Glide.with(this@ResultActivity)
                            .load(imageUrl)
                            .into(binding.ivFace)
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                Toast.makeText(this@ResultActivity, "Check your internet connection", Toast.LENGTH_SHORT).show()
            }

        })

    }
}