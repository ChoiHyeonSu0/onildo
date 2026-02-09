package com.likelion.onildo.ui.screen.write

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TilWriteScreen(
    viewModel: TilWriteViewModel = hiltViewModel(),
    // onNavigateBack: () -> Unit
    navController: NavHostController
) {
    var title by rememberSaveable { mutableStateOf("") }
    var learned by rememberSaveable { mutableStateOf("") }
    var difficulty by rememberSaveable { mutableStateOf("") }
    var tomorrow by rememberSaveable { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            Log.d("til", "til saved! title: $title, learned: $learned")
            // onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TIL 작성") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.saveTil(title, learned, difficulty, tomorrow)
                        },
                        enabled = title.isNotBlank() && learned.isNotBlank()
                    ) {
                        Text("저장")
                    }
                }
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
            // 제목
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("제목 *") },
                placeholder = { Text("오늘의 TIL 제목을 입력하세요") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 오늘 배운 것
            OutlinedTextField(
                value = learned,
                onValueChange = { learned = it },
                label = { Text("오늘 배운 것 *") },
                placeholder = { Text("오늘 무엇을 배웠나요?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 10
            )

            // 어려웠던 점
            OutlinedTextField(
                value = difficulty,
                onValueChange = { difficulty = it },
                label = { Text("어려웠던 점") },
                placeholder = { Text("어려웠던 부분이 있었나요?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 5
            )

            // 내일 할 일
            OutlinedTextField(
                value = tomorrow,
                onValueChange = { tomorrow = it },
                label = { Text("내일 할 일") },
                placeholder = { Text("내일은 무엇을 공부할 예정인가요?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 5
            )

            // 로딩 상태
            /*if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("AI가 분석 중입니다...")
                }
            }*/

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
