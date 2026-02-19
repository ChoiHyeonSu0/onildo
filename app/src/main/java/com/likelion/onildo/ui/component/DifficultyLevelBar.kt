package com.likelion.onildo.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class Difficulty(val label: String, val color: Color, val progress: Float) {
    EASY("쉬움", Color(0xFF4CAF50), 0.25f),      // 초록색
    NORMAL("보통", Color(0xFFFFDE00), 0.5f),    // 노랑색
    HARD("어려움", Color(0xFFFF9800), 0.75f),   // 주황색
    VERY_HARD("매우 어려움", Color(0xFFF44336), 1.0f); // 빨간색

    companion object {
        // String을 받아 Difficulty를 반환하는 함수
        fun fromLabel(label: String?): Difficulty {
            return entries.find { it.label == label } ?: NORMAL
            // 못 찾을 경우 기본값으로 NORMAL(보통)을 반환
        }
    }
}

@Composable
fun DifficultyLevelBar(difficulty: Difficulty) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val steps = Difficulty.entries.toTypedArray()

        Text("난이도: ", fontWeight = FontWeight.Bold)
        steps.forEach { step ->
            val isActive = step.ordinal <= difficulty.ordinal
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .background(
                        color = if (isActive) difficulty.color else Color.LightGray.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(difficulty.label)
        Spacer(modifier = Modifier.width(8.dp))
    }
}