package com.likelion.onildo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.likelion.onildo.ui.screen.MainScreen
import com.likelion.onildo.ui.screen.detail.TilDetailScreen
import com.likelion.onildo.ui.screen.write.TilWriteScreen

object AppDestinations {
    const val HOME = "home"
    const val STATS = "stats"
    const val ARCHIVE = "archive"
    const val MAIN = "main"
    const val DETAIL = "detail/{tilId}"
    const val WRITE = "write?tilId={tilId}"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

     NavHost(
         navController = navController,
         startDestination = AppDestinations.MAIN
     ) {
         composable(AppDestinations.MAIN) {
            MainScreen(navController = navController)
         }

         composable(
             route = AppDestinations.DETAIL,
             arguments = listOf(navArgument("tilId") { type = NavType.LongType } )
         ) { backStackEntry ->
             val tilId = backStackEntry.arguments?.getLong("tilId")
             TilDetailScreen(navController = navController, tilId = tilId)
         }

         composable(
             AppDestinations.WRITE,
             arguments = listOf(navArgument("tilId") {
                 type = NavType.LongType
                 defaultValue = -1L
             } )
         ) {
             TilWriteScreen(navController = navController)
         }
    }
}
