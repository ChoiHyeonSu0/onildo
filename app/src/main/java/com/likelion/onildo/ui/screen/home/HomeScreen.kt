package com.likelion.onildo.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.likelion.onildo.model.TilList
import com.likelion.onildo.ui.component.TilListCard
import com.likelion.onildo.ui.navigation.AppDestinations

// 임시 데이터 클래스
data class DailyGroup(val dateLabel: String, val records: List<TilList>)

@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    navController: NavHostController
) {
    // 임시 데이터
    val data = listOf(
        DailyGroup("오늘", listOf(TilList("😓", "Compose 심화 학습", "오후 1:09"))),
        DailyGroup("어제", listOf(
            TilList("😊", "Compose 기초 학습", "오후 2:15"),
            TilList("🎉", "Room DB 실습", "오전 10:44")
        ))
    )


    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("This is Home Screen.")

        LazyColumn(
            modifier = Modifier
                .padding()
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            data.forEach { group ->
                item {
                    Text(
                        text = group.dateLabel,
                        modifier = Modifier.padding(vertical = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(group.records) { record ->
                    TilListCard(record)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        Button(
            onClick = { rootNavController.navigate(AppDestinations.DETAIL) }
        ) {
            Text("TIL 상세 페이지")
        }
        Button(
            onClick = { rootNavController.navigate(AppDestinations.WRITE) }
        ) {
            Text("TIL 작성 페이지")
        }
    }
}