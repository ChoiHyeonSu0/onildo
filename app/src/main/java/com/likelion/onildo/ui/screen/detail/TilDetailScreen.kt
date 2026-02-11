package com.likelion.onildo.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun TilDetailScreen(
    navController: NavHostController,
    
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("😓 Compose 심화 학습", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("2026-02-03 오후 01:09:35", fontSize = 14.sp, color = Color.Gray)

        DetailSection(title = "배운 것", content = "컴포즈의 렌더링 단계인 Composition, Layout, Drawing의 메커니즘을 파악하며 성능 최적화 기법을 익혔습니다...")
        DetailSection(title = "어려웠던 점", content = "LaunchedEffect 같은 생명주기 활용 방법이나 UI 로직과 비즈니스 로직을 분리하는 과정이 어려웠습니다.")
        DetailSection(title = "내일 할 일", content = "배운 내용을 토대로 실습해보기")

        Spacer(modifier = Modifier.height(24.dp))
        // Divider()
        Spacer(modifier = Modifier.height(24.dp))

        Text("AI 분석 결과", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("감정 점수: ") }
            append("어려움")
        })
        Text(buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("코멘트: ") }
            append("기술적 어려움이 많이 보이네요!")
        })

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(32.dp))

        // 수정/삭제 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { /* 수정 화면 이동 */ },
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                shape = RoundedCornerShape(28.dp)
            ) { Text("수정") }
            Button(
                onClick = { /* 삭제 다이얼로그 */ },
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E)),
                shape = RoundedCornerShape(28.dp)
            ) { Text("삭제") }
        }
    }
}

@Composable
fun DetailSection(title: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = content, fontSize = 16.sp, lineHeight = 22.sp)
    }
}