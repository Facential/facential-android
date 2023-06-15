package com.capestone.facential.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class Data(

	@field:SerializedName("skincare_recommendation")
	val skincareRecommendation: String,

	@field:SerializedName("temperature")
	val temperature: Any,

	@field:SerializedName("pollution_index")
	val pollutionIndex: Any
)
