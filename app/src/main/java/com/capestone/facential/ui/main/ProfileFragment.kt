package com.capestone.facential.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capestone.facential.R
import com.capestone.facential.data.remote.model.History
import com.capestone.facential.data.remote.response.DeleteResponse
import com.capestone.facential.data.remote.response.HistoryResponse
import com.capestone.facential.data.remote.retrofit.ApiConfig
import com.capestone.facential.databinding.FragmentProfileBinding
import com.capestone.facential.ui.auth.LoginActivity
import com.capestone.facential.ui.result.DetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    private var historyCall: Call<HistoryResponse>? = null

    private val historyList: MutableList<History> = mutableListOf()

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
            val defaultImageUrl = R.mipmap.ic_launcher

            if (profilePictureUrl.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(profilePictureUrl)
                    .apply(RequestOptions())
                    .error(defaultImageUrl)
                    .into(binding.ivProfile)
            }

            val fullName = user.displayName

            binding.tvName.text = fullName
        }

        binding.btnLogout.setOnClickListener {
            signOut()
        }

        val userId = currentUser?.uid.toString()

        binding.btnDeleteAll.setOnClickListener {
            ApiConfig.getApiService().deleteAll(
                userId
            ).enqueue(object : Callback<DeleteResponse> {
                override fun onResponse(
                    call: Call<DeleteResponse>,
                    response: Response<DeleteResponse>
                ) {
                    historyList.clear()
                    historyAdapter.notifyDataSetChanged()
                    refreshFragment()
                }

                override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                }

            })
        }

        getHistoryList(userId)




        setupRecyclerViewHistory()

        return view
    }

    private fun getHistoryList(userId: String) {
        historyCall?.cancel()

        historyCall = ApiConfig.getApiService().getHistory(userId)
        historyCall?.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>
            ) {
                if (response.isSuccessful) {

                    binding.tvHistoryEmpty.visibility = View.INVISIBLE
                    binding.rvHistory.visibility = View.VISIBLE

                    val historyResponse = response.body()
                    val history = historyResponse?.history

                    history?.let {
                        historyList.clear()
                        historyList.addAll(it.map { historyItem ->
                            History(
                                skinType = historyItem.skinType,
                                date = historyItem.timestamp,
                                imageUrl = historyItem.imageUrl,
                                id = historyItem.id,
                                userId = historyItem.userUid
                            )
                        })

                        historyAdapter.setHistory(historyList)
                    }
                } else {
                    binding.tvHistoryEmpty.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.INVISIBLE
                }

            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {

            }

        })
    }

    private fun refreshFragment() {

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        val userId = currentUser?.uid.toString()

        historyList.clear()
        historyAdapter.notifyDataSetChanged()

        ApiConfig.getApiService().getHistory(
            userId
        ).enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>
            ) {
                if (response.isSuccessful) {

                    binding.tvHistoryEmpty.visibility = View.INVISIBLE
                    binding.rvHistory.visibility = View.VISIBLE

                    val historyResponse = response.body()
                    val history = historyResponse?.history

                    history?.let {
                        historyList.clear()
                        historyList.addAll(it.map { historyItem ->
                            History(
                                skinType = historyItem.skinType,
                                date = historyItem.timestamp,
                                imageUrl = historyItem.imageUrl,
                                id = historyItem.id,
                                userId = historyItem.userUid
                            )
                        })

                        historyAdapter.setHistory(historyList)
                    }
                } else {
                    binding.tvHistoryEmpty.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.INVISIBLE
                }

            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {

            }

        })
    }

    private fun setupRecyclerViewHistory() {
        recyclerView = binding.rvHistory
        historyAdapter = HistoryAdapter()

        recyclerView.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

            val spacingInPixels = (8 * resources.displayMetrics.density).toInt()
            val spaceItemDecoration = SpaceItemDecoration(spacingInPixels)
            addItemDecoration(spaceItemDecoration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        historyCall?.cancel()
        _binding = null
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
        requireActivity().finish()
    }
}