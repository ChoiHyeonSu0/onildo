package com.likelion.onildo.model


data class TilAnalysisResult(
    val emotion: String,
    val emotionScore: Int,
    val difficultyLevel: String,
    val comment: String
)