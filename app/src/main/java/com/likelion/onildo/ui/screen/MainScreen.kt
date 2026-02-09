package com.likelion.onildo.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.likelion.onildo.ui.component.BottomNavigationBar
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.likelion.onildo.ui.navigation.AppDestinations
import com.likelion.onildo.ui.screen.archive.ArchiveScreen
import com.likelion.onildo.ui.screen.home.HomeScreen
import com.likelion.onildo.ui.screen.stats.StatsScreen



@Composable
fun MainScreen(
    navController: NavHostController,
    // rootNavController: NavHostController
) {
    val innerNavController: NavHostController = rememberNavController()
    val currentBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
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