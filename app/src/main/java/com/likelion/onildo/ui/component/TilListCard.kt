package com.likelion.onildo.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.likelion.onildo.data.local.TilEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TilListCard(til: TilEntity) {
    val emoji = til.emotion!!.toEmoji()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD199FF)), // 보라색 배경
        border = CardDefaults.outlinedCardBorder() // 검은색 테두리 효과
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = til.title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = formatMillisToTime(til.createdAt), fontSize = 16.sp)
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

fun formatMillisToTime(millis: Long?): String {
    // 1. 출력 형식을 지정 (a: 오전/오후, hh: 12시간제 시, mm: 분)
    val sdf = SimpleDateFormat("a hh:mm", Locale.KOREAN)

   if(millis != null) {
       // 2. Long 타입의 밀리초를 Date 객체로 변환
       val date = Date(millis)

       // 3. 포맷 적용
       return sdf.format(date)
   }
    else return "작성 시간 정보 없음"
}

// 감정을 이모지로 변환하는 확장 함수
fun String.toEmoji(): String = when (this) {
    "성취감" -> "🎉"
    "만족" -> "😊"
    "평범" -> "😐"
    "어려움" -> "😓"
    "좌절" -> "😢"
    else -> "📝"
}