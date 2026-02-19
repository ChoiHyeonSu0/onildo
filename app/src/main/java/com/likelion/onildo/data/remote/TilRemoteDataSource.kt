package com.likelion.onildo.data.remote

import com.google.gson.Gson
import com.likelion.onildo.BuildConfig
import com.likelion.onildo.data.dto.ApiRequest
import com.likelion.onildo.model.TilAnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TilRemoteDataSource @Inject constructor(
    private val apiService: OpenAIApiService,
    private val gson: Gson
) {
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

        val requestBody = ApiRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(mapOf("role" to "user", "content" to prompt)),
            max_tokens = 200,
            temperature = 0.7
        )

        val response = apiService.analyzeTil("Bearer ${BuildConfig.OPEN_API_KEY}", requestBody)

        if (response.isSuccessful) {
            val apiResponse = response.body()
            // choices가 비어있는지 안전하게 확인
            val content = apiResponse?.choices?.firstOrNull()?.message?.content
                ?: throw Exception("No content from OpenAI")

            // content 내의 JSON 문자열을 객체로 파싱
            return@withContext gson.fromJson(content, TilAnalysisResult::class.java)
        } else {
            val errorMsg = response.errorBody()?.string()
            throw Exception("API Error: $errorMsg")
        }
    }
}