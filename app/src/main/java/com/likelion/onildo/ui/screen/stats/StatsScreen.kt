package com.likelion.onildo.ui.screen.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun StatsScreen( navController: NavHostController) {
    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("This is Stats Screen.")
    }
}