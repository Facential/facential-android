package com.capestone.facential.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("history")
	val history: List<HistoryItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class HistoryItem(

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("skin_type")
	val skinType: String,

	@field:SerializedName("timestamp")
	val timestamp: String,

	@field:SerializedName("user_uid")
	val userUid: String
)
