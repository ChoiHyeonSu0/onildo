package com.likelion.onildo.data.remote

import com.likelion.onildo.data.dto.ApiRequest
import com.likelion.onildo.data.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIApiService {
    @POST("v1/chat/completions")
    suspend fun analyzeTil(
        @Header("Authorization") auth: String, // Bearer $apiKey 전달
        @Body body: ApiRequest
    ): Response<ApiResponse>
}