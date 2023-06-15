package com.capestone.facential.data.remote.retrofit

import com.capestone.facential.data.remote.response.*
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

    @GET("getdata/{userId}/latest")
    fun getLatest(
        @Path("userId") userId: String
    ) : Call<ResultResponse>

    @GET("getdata/{userId}/{id}")
    fun getHistoryId(
        @Path("userId") userId: String,
        @Path("id") id: Int
    ) : Call<ResultResponse>

    @GET("getdata/{userId}")
    fun getHistory(
        @Path("userId") userId: String
    ) : Call<HistoryResponse>

    @DELETE("delete/{userId}")
    fun deleteAll(
        @Path("userId") userId: String
    ) : Call<DeleteResponse>

    @DELETE("delete/{userId}/{id}")
    fun deleteId(
        @Path("userId") userId: String,
        @Path("id") id: Int
    ) : Call<DeleteResponse>

    @Multipart
    @POST("recommendation")
    fun getRecommendation(
        @Part("latitude") lat: RequestBody,
        @Part("longitude")  lon: RequestBody
    ) : Call<RecommendationResponse>
}