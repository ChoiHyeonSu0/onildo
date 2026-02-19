package com.likelion.onildo.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.likelion.onildo.ui.component.BottomNavigationBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.likelion.onildo.ui.navigation.AppDestinations
import com.likelion.onildo.ui.screen.archive.ArchiveScreen
import com.likelion.onildo.ui.screen.home.HomeScreen
import com.likelion.onildo.ui.screen.stats.StatsScreen



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    // rootNavController: NavHostController
) {
    val innerNavController: NavHostController = rememberNavController()
    val currentBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("오닐도", fontWeight = FontWeight.Bold) },
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onTabSelected = { graphRoute ->
                    innerNavController.navigate(graphRoute) {
                        popUpTo(AppDestinations.HOME) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        floatingActionButton = {
            if(currentRoute == AppDestinations.HOME) {
                FloatingActionButton(onClick = {
                    navController.navigate("write")
                }) {
                    Icon(Icons.Default.Add, "새 TIL 작성")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = AppDestinations.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestinations.HOME) {
                HomeScreen(navController, innerNavController)
            }
            composable(AppDestinations.STATS) { StatsScreen(innerNavController) }
            composable(AppDestinations.ARCHIVE) { ArchiveScreen(innerNavController) }
        }
    }
}