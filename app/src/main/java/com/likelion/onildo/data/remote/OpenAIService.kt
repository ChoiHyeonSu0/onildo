package com.likelion.onildo.data.remote

import android.util.Log
import com.likelion.onildo.model.TilAnalysisResult
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OpenAIService(private val apiKey: String) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun analyzeTil(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?
    ): TilAnalysisResult = withContext(Dispatchers.IO) {

        val prompt = """
당신은 개발자 학습 코치입니다. 아래 TIL(Today I Learned) 내용을 분석해주세요.

[TIL 내용]
제목: $title
오늘 배운 것: $learned
어려웠던 점: ${difficulty ?: "없음"}
내일 할 일: ${tomorrow ?: "없음"}

[분석 요청]
다음 형식의 JSON으로만 응답해주세요:
{
    "emotion": "성취감/만족/평범/어려움/좌절 중 하나",
    "emotionScore": 1-5 사이 정수,
    "difficultyLevel": "쉬움/보통/어려움/매우 어려움 중 하나",
    "comment": "격려나 조언 한 문장 (20자 이내)"
}
""".trimIndent()

        val requestBody = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            })
            put("max_tokens", 200)
            put("temperature", 0.7)
        }


        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw Exception("Empty response")

        val jsonResponse = JSONObject(responseBody)
        val content = jsonResponse
            .getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content")

        // JSON 파싱
        val analysisJson = JSONObject(content)

        TilAnalysisResult(
            emotion = analysisJson.getString("emotion"),
            emotionScore = analysisJson.getInt("emotionScore"),
            difficultyLevel = analysisJson.getString("difficultyLevel"),
            comment = analysisJson.getString("comment")
        )
    }
}