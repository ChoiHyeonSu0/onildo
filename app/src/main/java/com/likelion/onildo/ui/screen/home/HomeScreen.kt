package com.likelion.onildo.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.likelion.onildo.ui.navigation.AppDestinations

@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    navController: NavHostController
) {
    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("This is Home Screen.")
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