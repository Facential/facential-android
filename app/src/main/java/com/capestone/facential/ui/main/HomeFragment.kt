package com.capestone.facential.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.capestone.facential.data.local.SharedPreferencesManager
import com.capestone.facential.data.remote.response.RecommendationResponse
import com.capestone.facential.data.remote.response.ResultResponse
import com.capestone.facential.data.remote.retrofit.ApiConfig
import com.capestone.facential.databinding.FragmentHomeBinding
import com.capestone.facential.ui.result.ResultActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    private var latestHistoryCall: Call<ResultResponse>? = null
    private var recommendationCall: Call<RecommendationResponse>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        val userName = currentUser?.displayName
        val firstName = userName?.split(" ")?.getOrNull(0)
        binding.tvHello.text = "Hi $firstName!"

        val userId = currentUser?.uid.toString()

        getLatestHistory(userId)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getLatLon()

        // Tracker Card View
        val currentDate = getCurrentDate()
        binding.tvDate.text = currentDate

        val sharedPreferencesManager = SharedPreferencesManager(requireContext())
        sharedPreferencesManager.resetSwitchStateIfNewDay()

        val previousStateDay = sharedPreferencesManager.getDaySwitchState()
        val previousStateNight = sharedPreferencesManager.getNightSwitchState()
        binding.btnSwitchDay.isChecked = previousStateDay
        binding.btnSwitchNight.isChecked = previousStateNight
    }

    private fun getLatestHistory(userId: String) {
        isLoadingLatest(true)
        latestHistoryCall?.cancel()

        latestHistoryCall = ApiConfig.getApiService().getLatest(userId)
        latestHistoryCall?.enqueue(object : Callback<ResultResponse> {
            override fun onResponse(
                call: Call<ResultResponse>,
                response: Response<ResultResponse>
            ) {
                if (response.isSuccessful) {
                    isLoadingLatest(false)
                    val result = response.body()

                    binding.tvHistoryEmpty.visibility = View.INVISIBLE
                    binding.cvLatest.visibility = View.VISIBLE

                    binding.tvCardHistoryTitle.text = result?.skinType

                    val imageUrl = result?.imageUrl

                    currentUser?.let {
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .centerCrop()
                            .into(binding.ivCardHistory)
                    }

                    binding.cvLatest.setOnClickListener {
                        startActivity(Intent(requireContext(), ResultActivity::class.java))
                    }
                } else {
                    isLoadingLatest(false)
                    binding.tvHistoryEmpty.visibility = View.VISIBLE
                    binding.cvLatest.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {

            }

        })
    }

    private fun getRecommendationData(lat: RequestBody, lon: RequestBody) {
        isLoadingRecommendation(true)
        recommendationCall?.cancel()

        recommendationCall = ApiConfig.getApiService().getRecommendation(lat, lon)
        recommendationCall?.enqueue(object : Callback<RecommendationResponse> {
            override fun onResponse(
                call: Call<RecommendationResponse>,
                response: Response<RecommendationResponse>
            ) {
                if (response.isSuccessful) {
                    isLoadingRecommendation(false)

                    val recommendationResponse = response.body()
                    val recommendationData = recommendationResponse?.data
                    binding.tvTemperature.text = recommendationData?.temperature.toString() + "C"
                    binding.tvSkincareRecommendation.text = recommendationData?.skincareRecommendation

                }
            }

            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {

            }

        })
    }

    private fun isLoadingRecommendation(showLoading: Boolean) {
        if (showLoading) {
            binding.cvRecommendation.visibility = View.INVISIBLE
            binding.loadingRecommendation.visibility = View.VISIBLE
        } else {
            binding.cvRecommendation.visibility = View.VISIBLE
            binding.loadingRecommendation.visibility = View.INVISIBLE
        }
    }

    private fun isLoadingLatest(showLoading: Boolean) {
        if (showLoading) {
            binding.cvLatest.visibility = View.INVISIBLE
            binding.tvHistoryEmpty.visibility = View.INVISIBLE
            binding.loadingLatest.visibility = View.VISIBLE
        } else {
            binding.cvLatest.visibility = View.VISIBLE
            binding.tvHistoryEmpty.visibility = View.VISIBLE
            binding.loadingLatest.visibility = View.INVISIBLE
        }
    }


    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun getLatLon() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val lat = it.latitude.toString()
                    val lon = it.longitude.toString()

                    val latRequestBody = lat.toRequestBody("text/plain".toMediaTypeOrNull())
                    val lonRequestBody = lon.toRequestBody("text/plain".toMediaTypeOrNull())

                    // Call the API with latitude and longitude as parameters
                    getRecommendationData(latRequestBody, lonRequestBody)
                } ?: run {
                    Toast.makeText(
                        requireContext(),
                        "Failed to retrieve location",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Failed to retrieve location: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        latestHistoryCall?.cancel()
        recommendationCall?.cancel()

        val sharedPreferencesManager = SharedPreferencesManager(requireContext())
        sharedPreferencesManager.saveDaySwitchState(binding.btnSwitchDay.isChecked)
        sharedPreferencesManager.saveNightSwitchState(binding.btnSwitchNight.isChecked)

        _binding = null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }


}