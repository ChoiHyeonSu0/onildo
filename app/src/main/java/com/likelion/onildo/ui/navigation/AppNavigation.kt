package com.likelion.onildo.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.likelion.onildo.ui.screen.MainScreen
import com.likelion.onildo.ui.screen.archive.ArchiveScreen
import com.likelion.onildo.ui.screen.detail.DetailScreen
import com.likelion.onildo.ui.screen.home.HomeScreen
import com.likelion.onildo.ui.screen.stats.StatsScreen
import com.likelion.onildo.ui.screen.write.TilWriteScreen

object AppDestinations {
    const val HOME = "home"
    const val STATS = "stats"
    const val ARCHIVE = "archive"
    const val MAIN = "main"
    const val DETAIL = "detail"
    const val WRITE = "write"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

     NavHost(
         navController = navController,
         startDestination = AppDestinations.MAIN
     ) {
         composable(AppDestinations.MAIN) {
            MainScreen(navController = navController)
         }

         composable(AppDestinations.DETAIL) {
             DetailScreen(navController = navController)
         }

         composable(AppDestinations.WRITE) {
             TilWriteScreen(navController = navController)
         }
    }
}

/*
rootNavController: NavHostController,
innerNavController: NavHostController,
innerPadding: PaddingValues,

NavHost(
        navController = innerNavController,
        startDestination = AppDestinations.HOME,
        modifier = Modifier.padding(innerPadding)
    )
*/
