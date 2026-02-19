package com.likelion.onildo.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.likelion.onildo.ui.component.TilListCard
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getTilList()
    }

    val tilListState by viewModel.tilListState.collectAsState()

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        when(val state = tilListState) {
            TilListState.Loading -> CircularProgressIndicator()
            is TilListState.Success -> {
                val tilList by state.tilList.collectAsState(initial = emptyMap())

                LazyColumn {
                    tilList.forEach { (date, tils) ->
                        item(date) {
                            Text(date, fontWeight = FontWeight.Bold)
                        }
                        items(tils) { item ->
                            TilListCard(item, rootNavController, { viewModel.deleteTil(item) })
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
            is TilListState.Error -> {
                Text(state.error)
            }
        }
    }
}