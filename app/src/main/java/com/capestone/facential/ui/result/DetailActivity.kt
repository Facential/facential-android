package com.capestone.facential.ui.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.capestone.facential.R
import com.capestone.facential.data.remote.model.Item
import com.capestone.facential.data.remote.response.DeleteResponse
import com.capestone.facential.data.remote.response.ResultResponse
import com.capestone.facential.data.remote.retrofit.ApiConfig
import com.capestone.facential.databinding.ActivityDetailBinding
import com.capestone.facential.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        val userId = currentUser?.uid.toString()
        val historyId = intent.getIntExtra("historyId", -1)

        binding.btnBack.setOnClickListener { finish() }

        ApiConfig.getApiService().getHistoryId(
            userId,
            historyId
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
                        Glide.with(this@DetailActivity)
                            .load(imageUrl)
                            .centerCrop()
                            .into(binding.ivFace)
                    }

                    // Cleansing
                    binding.tvCardCleansingDesc.text = result.cleansing

                    // Toner
                    binding.tvCardTonerDesc.text = result.toner

                    // Serum
                    binding.tvCardSerumDesc.text = result.serum

                    // Moisturizer
                    binding.tvCardMoisturizerDesc.text = result.moisturizer

                    // Sun Screen
                    binding.tvCardSunscreenDesc.text = result.sunscreen
                }
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
            }
        })

        binding.btnDelete.setOnClickListener {
            deleteData(userId, historyId)
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }


    }

    private fun deleteData(userId: String, historyId: Int) {
        ApiConfig.getApiService().deleteId(
            userId,
            historyId
        ).enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                Toast.makeText(this@DetailActivity, "Item Deleted", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Delete failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

}