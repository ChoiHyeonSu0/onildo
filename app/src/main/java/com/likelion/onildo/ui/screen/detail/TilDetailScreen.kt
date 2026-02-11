package com.likelion.onildo.ui.screen.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.likelion.onildo.ui.component.AIAnnotatedStringText
import com.likelion.onildo.ui.component.DetailSection
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TilDetailScreen(
    navController: NavHostController,
    viewModel: TilDetailViewModel = hiltViewModel(),
    tilId: Long?
) {
    val tilDetailState by viewModel.tilDetailState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getTilById(viewModel.tilId)
        viewModel.uiEvent.collect { event ->
            when(event) {
                DetailUiEvent.NavigateBack -> {
                    Toast.makeText(context, "학습 내용이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold { paddingValues ->
        when(val state = tilDetailState) {
            TilDetailState.Loading -> CircularProgressIndicator()

            is TilDetailState.Success -> {
                val til = state.til

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(til.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(millisToTime(til.createdAt), fontSize = 14.sp, color = Color.Gray)

                    DetailSection(
                        title = "배운 것",
                        content = til.learned
                    )
                    DetailSection(
                        title = "어려웠던 점",
                        content = til.difficulty ?: ""
                    )
                    DetailSection(
                        title = "내일 할 일",
                        content = til.tomorrow ?: ""
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("AI 분석 결과", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    AIAnnotatedStringText("감정 점수", til.emotion)
                    AIAnnotatedStringText("난이도", til.difficultyLevel)
                    AIAnnotatedStringText("코멘트", til.aiComment)

                    // Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Button(
                            onClick = {  },
                            modifier = Modifier.size(width = 120.dp, height = 60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text("수정", fontSize = 18.sp, color = Color.White)
                        }


                        Button(
                            onClick = { showDialog = true },
                            modifier = Modifier.size(width = 120.dp, height = 60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E)),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text("삭제", fontSize = 18.sp, color = Color.White)
                        }
                    }
                }

                if(showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false }, // 바깥 클릭 시 닫기
                        confirmButton = {
                            TextButton(onClick = {
                                showDialog = false
                                viewModel.deleteTil(til)
                            }) {
                                Text("확인")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("취소")
                            }
                        },
                        title = { Text(text = "알림") },
                        text = { Text(text = "정말로 이 학습 내용을 삭제하시겠습니까?") }
                    )
                }
            }

            is TilDetailState.Error -> {
                Text(state.error)
            }
        }
    }

}

fun millisToTime(millis: Long?): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd a hh:mm:ss", Locale.KOREAN)

    if(millis != null) {
        val date = Date(millis)

        return sdf.format(date)
    }
    else return "작성 시간 정보 없음"
}