package com.capestone.facential.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResultResponse(

	@field:SerializedName("moisturizer")
	val moisturizer: String,

	@field:SerializedName("sunscreen")
	val sunscreen: String,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("toner")
	val toner: String,

	@field:SerializedName("overall")
	val overall: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("serum")
	val serum: String,

	@field:SerializedName("skin_type")
	val skinType: String,

	@field:SerializedName("cleansing")
	val cleansing: String,

	@field:SerializedName("timestamp")
	val timestamp: String,

	@field:SerializedName("user_uid")
	val userUid: String
)
