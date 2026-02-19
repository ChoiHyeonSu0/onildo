package com.likelion.onildo.data.dto

data class ApiRequest(
    val model: String,
    val messages: List<Map<String, String>>,
    val max_tokens: Int,
    val temperature: Double
)

data class Message(val role: String, val content: String)

data class ApiResponse(
    val choices: List<Choice>
)

data class Choice(val message: Message)