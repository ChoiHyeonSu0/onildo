package com.likelion.onildo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tils")
data class TilEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val learned: String,           // 오늘 배운 것
    val difficulty: String?,       // 어려웠던 점
    val tomorrow: String?,         // 내일 할 일

    // AI 분석 결과
    val emotion: String?,          // 감정 (성취감, 만족, 평범, 어려움, 좌절)
    val emotionScore: Int?,        // 감정 점수 (1-5)
    val difficultyLevel: String?,  // 난이도 (쉬움, 보통, 어려움, 매우 어려움)
    val aiComment: String?,        // AI 한줄 코멘트

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)