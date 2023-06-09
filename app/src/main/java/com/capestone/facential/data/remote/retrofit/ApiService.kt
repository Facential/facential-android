package com.capestone.facential.data.remote.retrofit

import com.capestone.facential.data.remote.response.ClassifyResponse
import com.capestone.facential.data.remote.response.ResultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("classify")
    fun postFace(
        @Part("Authorization") uid: RequestBody,
        @Part file: MultipartBody.Part
    ) : Call<ClassifyResponse>

    @GET("classification_results/{userId}")
    fun getResult(
        @Path("userId") userId: String
    ) : Call<ResultResponse>
}