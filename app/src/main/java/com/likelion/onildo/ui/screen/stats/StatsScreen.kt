package com.likelion.onildo.ui.screen.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.likelion.onildo.ui.component.LabeledCard
import com.likelion.onildo.ui.theme.Purple40
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun StatsScreen(
    navController: NavHostController,
    viewModel: StatsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.analyzeChart()
    }

    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val statsState by viewModel.statsState.collectAsState()

        when(val state = statsState) {
            StatsState.Loading -> CircularProgressIndicator()
            is StatsState.Success ->  {
                Column() {
                    Text(
                        text = "최근 5일동안 학습한 나의 감정 트렌드 차트에요.",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Chart(
                        chart = lineChart(
                            lines = listOf(
                                lineSpec(
                                    lineColor = Color.Gray /*MaterialTheme.colorScheme.primary.toArgb()*/
                                )
                            )
                        ),
                        model = state.chartEntryModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        startAxis = rememberStartAxis(
                            valueFormatter = { value, _ ->
                                when (value.toInt()) {
                                    1 -> "😢"
                                    2 -> "😓"
                                    3 -> "😐"
                                    4 -> "😊"
                                    5 -> "🎉"
                                    else -> ""
                                }
                            }
                        ),
                        bottomAxis = rememberBottomAxis()
                    )
                }
            }
            is StatsState.Error -> Text(state.error)
        }

    }
}