package com.likelion.onildo.ui.screen.write

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.likelion.onildo.data.local.TilEntity
import com.likelion.onildo.ui.screen.detail.TilDetailState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TilWriteScreen(
    viewModel: TilWriteViewModel = hiltViewModel(),
    navController: NavHostController
) {
    /*var title by rememberSaveable { mutableStateOf("") }
    var learned by rememberSaveable { mutableStateOf("") }
    var difficulty by rememberSaveable { mutableStateOf("") }
    var tomorrow by rememberSaveable { mutableStateOf("") }*/

    val uiState by viewModel.uiState.collectAsState()
    val tilDetailState by viewModel.tilDetailState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(tilDetailState) {
        Log.d("til", viewModel.tilId.toString())
        if(viewModel.tilId != null) {
            viewModel.getTilById(viewModel.tilId)
        }
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            // Log.d("til", "til saved! title: $title, learned: $learned")
            Toast.makeText(context, "TIL 작성이 완료되었어요.", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TIL 작성") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("* 항목은 필수적으로 입력해야 합니다.")

            // 제목
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("제목 *") },
                placeholder = { Text("오늘의 TIL 제목을 입력하세요") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 오늘 배운 것
            OutlinedTextField(
                value = viewModel.learned,
                onValueChange = { viewModel.onLearnedChange(it) },
                label = { Text("오늘 배운 것 *") },
                placeholder = { Text("오늘 무엇을 배웠나요?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 10
            )

            // 어려웠던 점
            OutlinedTextField(
                value = viewModel.difficulty,
                onValueChange = { viewModel.onDifficultyChange(it) },
                label = { Text("어려웠던 점") },
                placeholder = { Text("어려웠던 부분이 있었나요?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 5
            )

            // 내일 할 일
            OutlinedTextField(
                value = viewModel.tomorrow,
                onValueChange = {viewModel.onTomorrowChange(it) },
                label = { Text("내일 할 일") },
                placeholder = { Text("내일은 무엇을 공부할 예정인가요?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 저장 버튼
                Button(
                    onClick = { viewModel.saveOrUpdateTil(
                        viewModel.title, viewModel.learned, viewModel.difficulty, viewModel.tomorrow) },
                    modifier = Modifier.size(width = 120.dp, height = 60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text("저장", fontSize = 18.sp, color = Color.White)
                }

                // 취소 버튼
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(width = 120.dp, height = 60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E)),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text("취소", fontSize = 18.sp, color = Color.White)
                }
            }

            // 로딩 상태
            if (uiState.isLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI가 분석 중입니다. 기다려 주세요..")
                }
            }

            // 에러 메시지
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/*
@Composable
fun TilWriteScreen( navController: NavHostController) {
    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Text("This is Write Screen.")
        }
    }
}*/
